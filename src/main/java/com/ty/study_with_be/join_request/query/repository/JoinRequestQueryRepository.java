package com.ty.study_with_be.join_request.query.repository;

public interface JoinRequestQueryRepository {

    boolean existsPendingJoin(Long studyGroupId, Long memberId);
}
