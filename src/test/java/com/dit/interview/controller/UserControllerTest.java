package com.dit.interview.controller;

import com.dit.interview.config.JwtAuthFilter;
import com.dit.interview.dto.req.LoginRequest;
import com.dit.interview.dto.req.UserRequest;
import com.dit.interview.entity.User;
import com.dit.interview.exception.InvalidCredentialException;
import com.dit.interview.exception.UserAlreadyExistsException;
import com.dit.interview.repository.UserRepository;
import com.dit.interview.service.UserService;
import com.dit.interview.utils.Constants;
import com.dit.interview.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UserController.class)
class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService service;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;


    UserRequest userRequest;

    @BeforeEach
    void setUp() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController(service, jwtUtil)).build();
        this.objectMapper = new ObjectMapper();

        userRequest = new UserRequest(
                "firstname",
                "lastname",
                "username",
                "password");
    }

    @Test
    void hello_shouldReturnHelloWorld() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("Hello, World!"));
    }


    @Test
    void create_shouldCreateUserSuccessfully() throws Exception {

        User user = new User();
        user.setId(1L);

        Mockito.when(service.create(Mockito.any(UserRequest.class)))
                .thenReturn(user);

        mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void create_shouldThrowUserAlreadyExist() throws Exception {

        User user = new User();
        user.setId(1L);

        Mockito.when(service.create(Mockito.any(UserRequest.class)))
                .thenThrow(new UserAlreadyExistsException("User already exist"));

        mockMvc.perform(
                        post("/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("User already exist"));
    }


    @Test
    void login_shouldSetJwtCookie() throws Exception {

        LoginRequest request = new LoginRequest(userRequest.userName(), userRequest.password());

        User user = new User();
        user.setUsername(request.userName());

        Mockito.when(service.authenticate(Mockito.any(LoginRequest.class)))
                .thenReturn(user);

        Mockito.when(jwtUtil.generateToken(request.userName()))
                .thenReturn("jwt-token");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists(Constants.COOKIE_NAME))
                .andExpect(cookie().value(Constants.COOKIE_NAME, "jwt-token"));
    }

    @Test
    void login_shouldFailWithBlackUsernameOrPassword() throws Exception {

        LoginRequest request = new LoginRequest(userRequest.userName(), "");

        User user = new User();
        user.setUsername(request.userName());

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("username and password required"));

    }

    @Test
    void login_shouldReturn5xxErrorForOtherUnknownError() throws Exception {

        LoginRequest request = new LoginRequest(userRequest.userName(), "wrong_password");

        Mockito.when(service.authenticate(Mockito.any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Something went wrong"));

        User user = new User();
        user.setUsername(request.userName());

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Something went wrong"));

    }

    @Test
    void login_shouldReturnUnauthorizedOnInvalidCredentials() throws Exception {

        LoginRequest request = new LoginRequest(userRequest.userName(), "wrong");

        Mockito.when(service.authenticate(Mockito.any(LoginRequest.class)))
                .thenThrow(new InvalidCredentialException("Invalid credentials"));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }


    @Test
    void authenticate_shouldReturnAuthorizedTrue() throws Exception {
        mockMvc.perform(
                        get("/authenticate")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.authorized").value(true));
    }
}
