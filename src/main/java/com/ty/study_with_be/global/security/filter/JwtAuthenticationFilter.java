package com.ty.study_with_be.global.security.filter;

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

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ") && jwtTokenProvider.validateToken(authHeader.substring(7))) {
            Authentication authentication = jwtTokenProvider.getAuthentication(authHeader.substring(7));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
