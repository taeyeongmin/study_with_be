package com.ty.study_with_be.join_request.application.command;

import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;

public interface ProcessJoinRequestUseCase {
    void process(Long studyGroupId, Long requestId, Long processorId,JoinRequestStatus status);
}
