package com.ty.study_with_be.member.repository;

import com.ty.study_with_be.member.entity.Member;
import com.ty.study_with_be.member.enums.AuthType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByAuthTypeAndProviderUserId(AuthType authType, String providerUserId);

    Optional<Member> findByAuthTypeAndProviderUserId(AuthType authType, String providerUserId);
}
