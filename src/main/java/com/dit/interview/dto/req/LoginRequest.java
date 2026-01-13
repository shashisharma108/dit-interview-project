package com.dit.interview.dto.req;

/**
 * LoginRequest
 * @param userName, username
 * @param password, password
 */
public record LoginRequest(
        String userName,
        String password) {
}
