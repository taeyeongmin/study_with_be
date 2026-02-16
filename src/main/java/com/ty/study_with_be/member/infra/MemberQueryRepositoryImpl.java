package com.ty.study_with_be.member.infra;

import com.ty.study_with_be.member.application.query.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepositoryImpl implements MemberQueryRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Optional<String> findNicknameById(Long memberId) {
        return memberJpaRepository.findNicknameById(memberId);
    }
}
