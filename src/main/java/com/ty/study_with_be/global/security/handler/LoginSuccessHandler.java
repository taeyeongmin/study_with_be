package com.ty.study_with_be.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ty.study_with_be.global.security.CustomUserDetails;
import com.ty.study_with_be.global.security.token.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        // JWT 발급
        String accessToken = jwtTokenProvider.createAccessToken(
                String.valueOf(user.getMemberId()),
                user.getAuthorities()
        );

        // 응답 바디(JSON)
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> body = Map.of(
                "memberId", user.getLoginId(),
                "nickname", user.getNickname(),
                "message", "로그인 성공",
                "accessToken", accessToken
        );

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}