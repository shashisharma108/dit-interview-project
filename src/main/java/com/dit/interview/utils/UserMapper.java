package com.dit.interview.utils;

import com.dit.interview.dto.req.UserRequest;
import com.dit.interview.dto.res.UserResponse;
import com.dit.interview.entity.User;

public class UserMapper {

    /**
     * Map UserRequest to User Entity
     * @param request, UserRequest
     * @return User Entity
     */
    public static User toEntity(UserRequest request) {
        if (request == null) {
            return null;
        }

        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setUsername(request.userName());
        user.setPassword(BcryptPasswordManager.hash(request.password()));

        return user;
    }

    /**
     * Converts User entity to UserResponse.
     * @param user, User entity object
     */
    public static UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getLastLoginTime());

    }
}
