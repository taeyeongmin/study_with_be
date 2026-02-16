package com.ty.study_with_be.global.outbox.application.support;

import com.ty.study_with_be.member.application.query.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberNicknameResolver {

    private final MemberQueryRepository memberQueryRepository;

    public String nicknameOf(Long memberId) {
        return memberQueryRepository.findNicknameById(memberId)
            .orElse("알 수 없음");
    }
}
