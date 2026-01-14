package com.ty.study_with_be.member.infra;

import com.ty.study_with_be.member.domain.model.Member;
import com.ty.study_with_be.member.domain.model.AuthType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    boolean existsByAuthTypeAndProviderUserId(AuthType authType, String providerUserId);

    Optional<Member> findByAuthTypeAndProviderUserId(AuthType authType, String providerUserId);

    Optional<Member> findByLoginId(String loginId);
}
