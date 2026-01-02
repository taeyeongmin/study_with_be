package com.ty.study_with_be.member.service;

import com.ty.study_with_be.member.dto.req.SignupReq;
import com.ty.study_with_be.member.entity.Member;
import com.ty.study_with_be.member.enums.AuthType;
import com.ty.study_with_be.member.repository.MemberRepository;
import com.ty.study_with_be.member.service.signup.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final Map<String, SignupService> signupServiceMap;

    public void register(SignupReq signupReq) {

        SignupService signupService = signupServiceMap.get(signupReq.getAuthType().name());

        // 1. 아이디 중복 체크(Local은 loginId, social은 provider+providerUserId로)
        signupService.validate(signupReq);

        // 2. memberEntity 생성(Local은 Member.createLocalMember(), social은 createSocialMember() )
        Member memberEntity = signupService.createMemberEntity(signupReq);

        // 3. 저장
        memberRepository.save(memberEntity);
    }
}
