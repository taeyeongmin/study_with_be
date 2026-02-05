package com.ty.study_with_be.join_request.domain.event;

import com.ty.study_with_be.global.event.domain.DomainEvent;
import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class JoinProcessEvent extends DomainEvent {

    private final Long studyGroupId;
    private final Long requesterMemberId;
    private final Long processorId;
    private final JoinRequestStatus joinRequestStatus;

    public JoinProcessEvent(Long studyGroupId, Long requesterMemberId, Long processorId, JoinRequestStatus joinRequestStatus) {
        this.eventId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.studyGroupId = studyGroupId;
        this.requesterMemberId = requesterMemberId;
        this.processorId = processorId;
        this.joinRequestStatus = joinRequestStatus;
    }

    public static JoinProcessEvent of(Long studyGroupId, Long requesterMemberId,Long processorId, JoinRequestStatus joinRequestStatus) {
        return new JoinProcessEvent(studyGroupId, requesterMemberId,processorId, joinRequestStatus);
    }

    public String getEventId(){
        return this.eventId;
    }
}