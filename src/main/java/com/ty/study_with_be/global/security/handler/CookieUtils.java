package com.ty.study_with_be.global.security.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public final class CookieUtils {

    public static void addHttpOnlyCookie(
            HttpServletResponse response,
            String name,
            String value,
            int maxAgeSeconds,
            boolean secure
    ) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeSeconds);
        response.addCookie(cookie);
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static void deleteCookie(HttpServletResponse response, String name, boolean secure) {
        Cookie cookie = new Cookie(name, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
