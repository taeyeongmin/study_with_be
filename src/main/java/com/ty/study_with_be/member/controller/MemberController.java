package com.ty.study_with_be.member.controller;

import com.ty.study_with_be.member.dto.req.SignupReq;
import com.ty.study_with_be.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public void registerMember(@Valid @RequestBody SignupReq signupReq) {
        memberService.register(signupReq);
    }
}
