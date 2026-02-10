package com.ty.study_with_be.member.infra;

import com.ty.study_with_be.member.domain.model.Member;
import com.ty.study_with_be.member.domain.model.AuthType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    boolean existsByAuthTypeAndProviderUserId(AuthType authType, String providerUserId);

    Optional<Member> findByAuthTypeAndProviderUserId(AuthType authType, String providerUserId);

    Optional<Member> findByLoginId(String loginId);

    Member findByMemberId(Long memberId);

    @Query("select m.nickname from Member m where m.memberId = :memberId")
    Optional<String> findNicknameById(Long memberId);

    boolean existsByNickname(String nickname);
}
