package com.ty.study_with_be.join_request.domain.event;

import com.ty.study_with_be.global.event.domain.DomainEvent;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class JoinApproveEvent extends DomainEvent {

    private final Long studyGroupId;
    private final Long requesterMemberId;
    private final Long processorId;

    public JoinApproveEvent(Long studyGroupId, Long requesterMemberId, Long processorId) {
        this.eventId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.studyGroupId = studyGroupId;
        this.requesterMemberId = requesterMemberId;
        this.processorId = processorId;
    }

    public static JoinApproveEvent of(Long studyGroupId, Long requesterMemberId, Long processorId) {
        return new JoinApproveEvent(studyGroupId, requesterMemberId,processorId);
    }

    public String getEventId(){
        return this.eventId;
    }
}