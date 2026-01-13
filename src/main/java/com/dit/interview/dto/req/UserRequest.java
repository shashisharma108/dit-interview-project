package com.dit.interview.dto.req;


/**
 * UserRequest payload required to create new user
 * @param firstName, firstName
 * @param lastName, lastName
 * @param userName, userName
 * @param password, password
 */
public record UserRequest (
        String firstName,
        String lastName,
        String userName,
        String password) {
}
