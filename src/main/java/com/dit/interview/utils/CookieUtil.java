package com.dit.interview.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Optional;

public class CookieUtil {

    /**
     * Retrieve cookies from request
     * @param request, HttpServletRequest
     * @param name, cookie name to be retrieved
     * @return return cookie value
     */
    public static String getTokenFromCookie( HttpServletRequest request, String name) {

        if (request.getCookies() == null) {
            return null;
        }

        Optional<Cookie> optionalCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> name.equals(cookie.getName()))
                .findFirst();

        return optionalCookie.map(Cookie::getValue).orElse(null);

    }

}
