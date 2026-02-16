package com.ty.study_with_be.member.application.command;

import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.member.domain.model.Member;
import com.ty.study_with_be.member.domain.repository.MemberRepository;
import com.ty.study_with_be.member.presentation.command.dto.UpdateProfileReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateProfileService implements UpdateProfileUseCase{

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void update(Long memberId, UpdateProfileReq req) {

        // 닉네임 중복 검증
        if (memberRepository.existsByNickname(req.getNickname()))
            throw new DomainException(ErrorCode.DUPLICATE_NICKNAME);

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member Not Found"));
        member.changeProfile(req.getNickname(), req.getEmail());
    }
}
