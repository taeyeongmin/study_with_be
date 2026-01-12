package com.ty.study_with_be.member.service;

import com.ty.study_with_be.member.presentation.res.MemberInfoRes;
import com.ty.study_with_be.member.domain.model.Member;
import com.ty.study_with_be.member.domain.model.AuthType;
import com.ty.study_with_be.member.repository.MemberRepository;
import com.ty.study_with_be.member.service.signup.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final Map<String, SignupService> signupServiceMap;

    @Transactional
    public void register(SignupReq signupReq) {

        SignupService signupService = signupServiceMap.get(signupReq.getAuthType().name());

        // 1. 아이디 중복 체크(Local은 loginId, social은 provider+providerUserId로)
        signupService.validate(signupReq);

        // 2. memberEntity 생성(Local은 Member.createLocalMember(), social은 createSocialMember() )
        Member memberEntity = signupService.createMemberEntity(signupReq);

        // 3. 저장
        memberRepository.save(memberEntity);
    }

    public MemberInfoRes getMemberInfo(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("회원 없음"));

        return new MemberInfoRes(
                member.getMemberId(),
                member.getNickname(),
                member.getEmail()
        );
    }

    public boolean existsSocialMember(AuthType authType, String providerUserId) {
        return memberRepository.existsByAuthTypeAndProviderUserId(authType, providerUserId);
    }

    public Member findSocialMember(AuthType authType, String providerUserId) {
        return memberRepository.findByAuthTypeAndProviderUserId(authType, providerUserId)
                .orElseThrow(() -> new IllegalStateException("Member not found."));
    }
}
