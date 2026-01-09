package com.ty.study_with_be.member.controller;

import com.ty.study_with_be.member.dto.req.SignupReq;
import com.ty.study_with_be.member.dto.res.MemberInfoRes;
import com.ty.study_with_be.member.service.MemberService;
import com.ty.study_with_be.security.handler.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<String> registerMember(@Valid @RequestBody SignupReq signupReq) {
        memberService.register(signupReq);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberInfoRes> memberInfo(@AuthenticationPrincipal User principal){
        Long memberId = Long.parseLong(principal.getUsername());
        MemberInfoRes memberInfo = memberService.getMemberInfo(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(memberInfo);
    }

}
