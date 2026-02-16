package com.ty.study_with_be.join_request.application.command;

public interface JoinRequestCancelUseCase {
    void cancel(Long studyGroupId, Long joinRequestId, Long currentMemberId);
}
