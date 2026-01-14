package com.ty.study_with_be.global.security.handler;

import com.ty.study_with_be.auth.presentation.req.SignupReq;
import com.ty.study_with_be.global.security.token.JwtTokenProvider;
import com.ty.study_with_be.member.domain.model.AuthType;
import com.ty.study_with_be.member.domain.model.Member;
import com.ty.study_with_be.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private static final String REDIRECT_URL = "http://localhost:3000/auth/popup-callback";

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

        String nickname = "kakao_" + providerUserId;
        String email = null;
        Object accountObj = attributes.get("kakao_account");
        if (accountObj instanceof Map<?, ?> account) {
            Object profileObj = account.get("profile");
            if (profileObj instanceof Map<?, ?> profile) {
                Object nickObj = profile.get("nickname");
                if (nickObj != null) {
                    nickname = String.valueOf(nickObj);
                }
            }
            Object emailObj = account.get("email");
            if (emailObj != null) {
                email = String.valueOf(emailObj);
            }
        }

        if (!memberService.existsSocialMember(AuthType.KAKAO, providerUserId)) {
            SignupReq signupReq = new SignupReq();
            signupReq.setAuthType(AuthType.KAKAO);
            signupReq.setProviderUserId(providerUserId);
            signupReq.setNickname(nickname);
            memberService.register(signupReq);
        }

        Member member = memberService.findSocialMember(AuthType.KAKAO, providerUserId);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String accessToken = jwtTokenProvider.createAccessToken(
                String.valueOf(member.getMemberId()),
                authorities
        );

        boolean secure = false;
        CookieUtils.addHttpOnlyCookie(response, "ACCESS_TOKEN", accessToken, 60 * 15, secure);
        response.sendRedirect(REDIRECT_URL);
    }
}
