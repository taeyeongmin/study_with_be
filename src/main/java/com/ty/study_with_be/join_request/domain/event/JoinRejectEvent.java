package com.ty.study_with_be.join_request.domain.event;

import com.ty.study_with_be.global.event.domain.DomainEvent;
import com.ty.study_with_be.join_request.domain.model.enums.RejectionReason;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class JoinRejectEvent extends DomainEvent {

    private final Long studyGroupId;
    private final Long requesterMemberId;
    private final Long processorId;
    private final RejectionReason rejectionReason;

    public JoinRejectEvent(Long studyGroupId, Long requesterMemberId, Long processorId, RejectionReason rejectionReason) {
        this.eventId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.studyGroupId = studyGroupId;
        this.requesterMemberId = requesterMemberId;
        this.processorId = processorId;
        this.rejectionReason = rejectionReason;
    }

    public static JoinRejectEvent of(Long studyGroupId, Long requesterMemberId, Long processorId, RejectionReason rejectionReason) {
        return new JoinRejectEvent(studyGroupId, requesterMemberId,processorId, rejectionReason);
    }

    public String getEventId(){
        return this.eventId;
    }
}