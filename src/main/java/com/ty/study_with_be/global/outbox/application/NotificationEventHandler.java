package com.ty.study_with_be.global.outbox.application;

import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.global.outbox.application.dto.OutboxPayload;
import com.ty.study_with_be.join_request.domain.model.enums.RejectionReason;

import java.util.Set;

/**
 * notification 관련 처리를 위한 interface로
 * 실제 구현은 notification에 위치
 */
public interface NotificationEventHandler {

    void precess(EventType type, OutboxPayload outboxPayload, RejectionReason rejectionReason) throws Exception;
}
