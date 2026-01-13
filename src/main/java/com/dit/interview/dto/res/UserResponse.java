package com.dit.interview.dto.res;

import java.time.LocalDateTime;

/**
 * User Response object, It can be used when all user info need to be returned to caller
 * @param userId, userId
 * @param firstName, firstName
 * @param lastName, lastName
 * @param userName, userName
 * @param lastLoginTime, lastLoginTime
 */
public record UserResponse(
        Long userId,
        String firstName,
        String lastName,
        String userName,
        LocalDateTime lastLoginTime) {
}
