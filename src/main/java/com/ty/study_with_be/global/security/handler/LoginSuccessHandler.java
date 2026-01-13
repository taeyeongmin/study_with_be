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

import java.io.IOException;
import java.util.Map;

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

        // HttpOnly 쿠키 생성
        Cookie cookie = new Cookie("ACCESS_TOKEN", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60); // 1시간 (초 단위)

        // secure 옵션은 배포 환경에서 true
        cookie.setSecure(false);

        response.addCookie(cookie);

        // 응답 바디(JSON)
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = Map.of(
                "memberId", user.getMemberId(),
                "nickname", user.getNickname(),
                "message", "로그인 성공"
        );

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}