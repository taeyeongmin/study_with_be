package com.ty.study_with_be.member.infra;

import com.ty.study_with_be.member.domain.model.AuthType;
import com.ty.study_with_be.member.domain.model.Member;
import com.ty.study_with_be.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return memberJpaRepository.findByLoginId(loginId);
    }

    @Override
    public void save(Member member) {
        memberJpaRepository.save(member);
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberJpaRepository.findById(memberId);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return memberJpaRepository.existsByNickname(nickname);
    }

    @Override
    public boolean existsByAuthTypeAndProviderUserId(AuthType authType, String providerUserId) {
        return memberJpaRepository.existsByAuthTypeAndProviderUserId(authType, providerUserId);
    }

    @Override
    public Optional<Member> findByAuthTypeAndProviderUserId(AuthType authType, String providerUserId) {
        return memberJpaRepository.findByAuthTypeAndProviderUserId(authType, providerUserId);
    }
}
