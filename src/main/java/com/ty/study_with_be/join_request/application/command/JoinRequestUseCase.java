package com.ty.study_with_be.join_request.application.command;

public interface JoinRequestUseCase {
    void requestJoin(Long studyGroupId, Long memberId);
}
