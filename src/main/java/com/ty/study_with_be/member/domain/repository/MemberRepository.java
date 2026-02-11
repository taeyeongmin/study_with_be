package com.ty.study_with_be.member.domain.repository;

import com.ty.study_with_be.member.domain.model.AuthType;
import com.ty.study_with_be.member.domain.model.Member;

import java.util.Optional;

public interface MemberRepository {

    boolean existsByAuthTypeAndProviderUserId(AuthType authType, String providerUserId);

    Optional<Member> findByAuthTypeAndProviderUserId(AuthType authType, String providerUserId);

    Optional<Member> findByLoginId(String loginId);

    void save(Member member);

    Optional<Member> findById(Long memberId);

    boolean existsByNickname(String nickname);
}
