package com.ty.study_with_be.member.domain.repository;

import com.ty.study_with_be.member.domain.model.Member;

import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findByLoginId(String loginId);

    void save(Member member);
}
