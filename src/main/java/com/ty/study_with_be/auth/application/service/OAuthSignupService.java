package com.ty.study_with_be.auth.application.service;

import com.ty.study_with_be.auth.application.OAuthSignupUseCase;
import com.ty.study_with_be.auth.presentation.req.OAuthSignupReq;
import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.member.domain.model.Member;
import com.ty.study_with_be.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OAuthSignupService implements OAuthSignupUseCase {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void register(OAuthSignupReq signupReq) {

        if (memberRepository.existsByNickname(signupReq.getNickname()))
            throw new DomainException(ErrorCode.DUPLICATE_NICKNAME);

        Member member = Member.createSocialMember(signupReq.getAuthType(),signupReq.getProviderUserId(),signupReq.getNickname());
        memberRepository.save(member);
    }
}
