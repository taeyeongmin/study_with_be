package com.ty.study_with_be.member.service;

import com.ty.study_with_be.member.domain.model.AuthType;
import com.ty.study_with_be.member.domain.model.Member;
import com.ty.study_with_be.member.domain.repository.MemberRepository;
import com.ty.study_with_be.member.presentation.query.dto.MemberInfoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberInfoRes getMemberInfo(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("회원 없음"));

        String authType = AuthType.KAKAO.equals(member.getAuthType()) ? "KAKAO" : "자사";

        return new MemberInfoRes(
                member.getMemberId(),
                member.getNickname(),
                member.getEmail(),
                authType
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
