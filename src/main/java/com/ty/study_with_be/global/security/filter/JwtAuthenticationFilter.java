package com.ty.study_with_be.global.security.filter;

import com.ty.study_with_be.global.security.handler.CookieUtils;
import com.ty.study_with_be.global.security.token.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 매 요청마다 토큰 값 검증하는 필터
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String ACCESS_TOKEN_COOKIE = "ACCESS_TOKEN";
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = CookieUtils.getCookieValue(request, ACCESS_TOKEN_COOKIE);

//        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = request.getHeader("Authorization");

        System.out.println(">>> METHOD = " + request.getMethod());
        System.out.println(">>> URI = " + request.getRequestURI());
        System.out.println(">>> Authorization = " + request.getHeader("Authorization"));

        if (token != null && jwtTokenProvider.validateToken(token)){
//        if (token != null && token.startsWith("Bearer ") && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
