package com.ty.study_with_be.global.security.handler;

import com.ty.study_with_be.auth.presentation.req.SignupReq;
import com.ty.study_with_be.global.security.token.JwtTokenProvider;
import com.ty.study_with_be.global.security.token.OAuth2ExchangeTokenStore;
import com.ty.study_with_be.global.security.token.OAuth2TicketPayload;
import com.ty.study_with_be.member.domain.model.AuthType;
import com.ty.study_with_be.member.domain.model.Member;
import com.ty.study_with_be.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final OAuth2ExchangeTokenStore oAuth2ExchangeTokenStore;

    @Value("${kakao.redirect-url}")
    private String REDIRECT_URL;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User principal = (OAuth2User) authentication.getPrincipal();

        Map<String, Object> attributes = principal.getAttributes();
        Object kakaoId = attributes.get("id");
        if (kakaoId == null) {
            throw new IllegalStateException("Kakao id is missing from OAuth2 attributes.");
        }
        String providerUserId = String.valueOf(kakaoId);

        // 기존 소셜 회원 여부 검증 후 없으면 oAuth 인증 정보만 담아서 리다이렉트
        if (!memberService.existsSocialMember(AuthType.KAKAO, providerUserId)) {

            String url = REDIRECT_URL + "?authType=" + AuthType.KAKAO+"&providerUserId="+providerUserId;
            response.sendRedirect(url);
            return;
        }

        Member member = memberService.findSocialMember(AuthType.KAKAO, providerUserId);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String accessToken = jwtTokenProvider.createAccessToken(
                String.valueOf(member.getMemberId()),
                authorities
        );

        String ticket = saveOAuthTicket(member.getMemberId(), accessToken, member.getNickname());

        String url = REDIRECT_URL + "?ticket=" + URLEncoder.encode(ticket, StandardCharsets.UTF_8);
        response.sendRedirect(url);
    }

    private String saveOAuthTicket(Long memberId, String accessToken,String nickname) {

        String ticket = UUID.randomUUID().toString();

        oAuth2ExchangeTokenStore.save(
                ticket,
                new OAuth2TicketPayload(accessToken, memberId, nickname),
                Duration.ofSeconds(30)
        );

        return ticket;
    }
}
