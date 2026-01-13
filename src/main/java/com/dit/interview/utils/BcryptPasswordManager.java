package com.dit.interview.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Handles password hashing and verification using BCrypt.
 */
public class BcryptPasswordManager  {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);


    /**
     * Hash (encrypt) a raw password.
     * @param rawPassword, user provided password for hashing
     */
    public static String hash(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * Verify raw password against stored hash.
     * @param hashedPassword, Hashed password from DB
     * @param rawPassword , user provided password
     */
    public static boolean matches(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
}
