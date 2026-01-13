
package com.dit.interview.service;

import com.dit.interview.dto.req.LoginRequest;
import com.dit.interview.exception.InvalidCredentialException;
import com.dit.interview.exception.UserAlreadyExistsException;
import com.dit.interview.dto.req.UserRequest;
import com.dit.interview.utils.UserMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import com.dit.interview.entity.User;
import com.dit.interview.repository.UserRepository;
import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    /**
     * Create user
     * @param userRequest, UserRequest that has all user info
     * @return newly created user
     */
    public User create(UserRequest userRequest) {
        if (repo.findByUsername(userRequest.userName()).isPresent()) {
            throw new UserAlreadyExistsException("User already exist by username : " + userRequest.userName());
        }
        User user = UserMapper.toEntity(userRequest);
        return repo.save(user);
    }

    /**
     * Authenticate user
     * @param loginRequest, LoginRequest containing username and password
     * @return authentictaed  user
     */
    public User authenticate(LoginRequest loginRequest) {

        if (StringUtils.isEmpty(loginRequest.userName()) || StringUtils.isEmpty(loginRequest.password())) {
            throw new InvalidCredentialException("Username or password can not be blank");
        }

        User user = repo.findByUsername(loginRequest.userName())
                .orElseThrow(() -> new InvalidCredentialException("Invalid credentials"));

        if (!BCrypt.checkpw(loginRequest.password(), user.getPassword())) {
            throw new InvalidCredentialException("Invalid credentials");
        }

        user.setLastLoginTime(LocalDateTime.now());
        repo.save(user);

        return user;
    }
}
