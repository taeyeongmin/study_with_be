package com.ty.study_with_be.notification.application.command.strategy;

import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.global.outbox.application.dto.OutboxPayload;
import com.ty.study_with_be.join_request.domain.model.enums.RejectionReason;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationContext{

    private Long studyGroupId;
    private Long processMemberId;
    private Long targetMemberId;
    private Long requesterMemberId;
    private EventType eventType;
    private RejectionReason rejectionReason;

    public static NotificationContext create(EventType eventType, OutboxPayload outboxPayload, RejectionReason rejectionReason){

        return new NotificationContext(
                outboxPayload.getStudyGroupId()
                , outboxPayload.getProcessMemberId()
                , outboxPayload.getTargetMemberId()
                , outboxPayload.getRequesterMemberId()
                , eventType
                , rejectionReason
        );
    }
}

