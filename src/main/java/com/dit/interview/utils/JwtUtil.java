
package com.dit.interview.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Utility class for JWT creation and validation.
 */
@Component
public class JwtUtil {

    // Minimum 32 characters required for HS256
    private static final String KEY = "a-string-secret-at-least-256-bits-long";

    private static final long EXPIRATION_TIME = 60 * 60 * 1000; // 1 hour

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            KEY.getBytes(StandardCharsets.UTF_8)
    );

    /**
     * Generates JWT token for given username.
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /** Validate token and return username */
    public String validateAndGetUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token) // expiry validated here
                .getBody();

        return claims.getSubject();
    }

}
