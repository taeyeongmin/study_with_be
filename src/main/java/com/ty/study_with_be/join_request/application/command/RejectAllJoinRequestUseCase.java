package com.ty.study_with_be.join_request.application.command;

public interface RejectAllJoinRequestUseCase {

    void rejectAll(Long studyGroupId, Long processorMemberId);
}
