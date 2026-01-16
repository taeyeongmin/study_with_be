package com.ty.study_with_be.auth.application.service;

import com.ty.study_with_be.auth.application.SignupUseCase;
import com.ty.study_with_be.auth.presentation.req.SignupReq;
import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.member.domain.model.Member;
import com.ty.study_with_be.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService implements SignupUseCase {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(SignupReq signupReq) {

        memberRepository.findByLoginId(signupReq.getLoginId()).ifPresent((m) -> {
            throw new DomainException(ErrorCode.DUPLICATE_LOGIN_ID);
        });

        Member member = Member.createLocalMember(signupReq.getLoginId(),passwordEncoder.encode(signupReq.getPassword()), signupReq.getNickname());
        memberRepository.save(member);
    }
}
