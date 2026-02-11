package com.ty.study_with_be.member.application.query.repository;

import java.util.Optional;

public interface MemberQueryRepository {

    Optional<String> findNicknameById(Long memberId);
}
