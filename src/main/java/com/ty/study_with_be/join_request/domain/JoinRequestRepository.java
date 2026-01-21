package com.ty.study_with_be.join_request.domain;

public interface JoinRequestRepository {
    boolean existsPending(Long groupId, Long memberId);
}
