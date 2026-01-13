
package com.dit.interview.controller;

import java.util.Map;

import com.dit.interview.dto.req.LoginRequest;
import com.dit.interview.dto.req.UserRequest;
import com.dit.interview.exception.InvalidCredentialException;
import com.dit.interview.utils.Constants;
import com.dit.interview.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.dit.interview.dto.res.ApiResponse;
import com.dit.interview.entity.User;
import com.dit.interview.service.UserService;

@Slf4j
@RestController
public class UserController {

    private final UserService service;
    private final JwtUtil jwtUtil;

    public UserController(UserService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }



    @GetMapping("/hello")
    public ApiResponse<String> hello() {
        return ApiResponse.success("Hello, World!");
    }


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Map<String, Long>>> create(@RequestBody UserRequest userRequest) {

        try {

            if (StringUtils.hasText(userRequest.userName()) &&  StringUtils.hasText(userRequest.password())) {
                log.info("User create : {}", userRequest);
                User u = service.create(userRequest);
                return ResponseEntity.ok(ApiResponse.success(Map.of("id", u.getId())));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("username and password required"));
            }

        } catch (Exception e) {
           log.error("User creation failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }

    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        try {
            if (StringUtils.hasText(loginRequest.userName()) &&  StringUtils.hasText(loginRequest.password())) {

                User loginUser = service.authenticate(loginRequest);

                if (loginUser != null) { // login success
                    String token = jwtUtil.generateToken(loginUser.getUsername());
                    Cookie jwtCookie = new Cookie(Constants.COOKIE_NAME, token);
                    jwtCookie.setHttpOnly(true);       // Prevent JS access (important)
                    jwtCookie.setSecure(false);        // true in HTTPS (prod), false for local
                    jwtCookie.setPath("/authenticate");// Send cookie to authenticate endpoints
                    jwtCookie.setMaxAge(30 * 60);      // 1 hour (seconds)

                    response.addCookie(jwtCookie);

                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("username and password required"));
            }

            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (InvalidCredentialException e) {

            log.error("... Failed to login", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(e.getMessage()));
        }
    }


    @GetMapping("/authenticate")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> auth() {
        return ResponseEntity.ok(ApiResponse.success(Map.of("authorized", true)));
    }
}
