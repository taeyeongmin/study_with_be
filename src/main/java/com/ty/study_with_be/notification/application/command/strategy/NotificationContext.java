package com.ty.study_with_be.notification.application.command.strategy;

import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.global.outbox.application.dto.OutboxPayload;
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

    public static NotificationContext create(EventType eventType, OutboxPayload outboxPayload){

        return new NotificationContext(
                outboxPayload.getStudyGroupId()
                , outboxPayload.getProcessMemberId()
                , outboxPayload.getTargetMemberId()
                , outboxPayload.getRequesterMemberId()
                , eventType
        );
    }
}

