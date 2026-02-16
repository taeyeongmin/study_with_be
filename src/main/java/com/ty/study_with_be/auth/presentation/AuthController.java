package com.ty.study_with_be.auth.presentation;

import com.ty.study_with_be.auth.application.OAuthSignupUseCase;
import com.ty.study_with_be.auth.application.SignupUseCase;
import com.ty.study_with_be.auth.presentation.req.OAuthSignupReq;
import com.ty.study_with_be.auth.presentation.req.SignupReq;
import com.ty.study_with_be.auth.presentation.res.OAuth2TicketRes;
import com.ty.study_with_be.global.security.handler.CookieUtils;
import com.ty.study_with_be.global.security.token.OAuth2ExchangeTokenStore;
import com.ty.study_with_be.global.security.token.OAuth2TicketPayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "인증/인가", description = "로그인 관련 API")
public class AuthController {

    private final SignupUseCase signupUseCase;
    private final OAuthSignupUseCase oAuthSignupUseCase;
    private final OAuth2ExchangeTokenStore ticketStore;

    @PostMapping("/signup_local")
    public ResponseEntity<Object> registerMember(@Valid @RequestBody SignupReq signupReq) {
        signupUseCase.register(signupReq);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup_oauth")
    public ResponseEntity<Object> registerMemberByOAuth(@Valid @RequestBody OAuthSignupReq signupReq) {
        oAuthSignupUseCase.register(signupReq);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        CookieUtils.deleteCookie(response, "ACCESS_TOKEN", false);
        return ResponseEntity.ok().build();
    }

    @PermitAll
    @GetMapping("/oauth2/exchange")
    @Operation(
            summary = "oAuth 로그인 토큰 교환",
            description = """
                    ## 기능 설명
                    - 해당 서비스의 token과 교환 가능한 ticket을 통해 token을 조회.
                    """
    )
    public ResponseEntity<OAuth2TicketRes> exchange(@RequestParam("ticket") String ticket) {

        OAuth2TicketPayload payload = ticketStore.consume(ticket)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired ticket"));

        return ResponseEntity.ok(
                new OAuth2TicketRes(
                        payload.getAccessToken(),
                        payload.getMemberId(),
                        payload.getNickname()
                )
        );
    }



}
