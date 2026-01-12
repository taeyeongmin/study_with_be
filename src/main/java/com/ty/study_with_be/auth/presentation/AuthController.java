package com.ty.study_with_be.auth.presentation;

import com.ty.study_with_be.auth.application.SignupUseCase;
import com.ty.study_with_be.auth.presentation.req.SignupReq;
import com.ty.study_with_be.global.security.handler.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final SignupUseCase signupUseCase;

    @PostMapping("/signup")
    public ResponseEntity<String> registerMember(@Valid @RequestBody SignupReq signupReq) {
        signupUseCase.register(signupReq);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        CookieUtils.deleteCookie(response, "ACCESS_TOKEN", false);
        return ResponseEntity.ok().build();
    }

}
