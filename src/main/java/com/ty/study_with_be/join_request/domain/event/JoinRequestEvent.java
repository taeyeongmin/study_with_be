package com.ty.study_with_be.join_request.domain.event;

import com.ty.study_with_be.global.event.domain.DomainEvent;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class JoinRequestEvent extends DomainEvent {

    private final Long studyGroupId;
    private final Long requesterMemberId;

    public JoinRequestEvent(Long studyGroupId, Long requesterMemberId) {
        this.eventId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.studyGroupId = studyGroupId;
        this.requesterMemberId = requesterMemberId;
    }

    public static JoinRequestEvent of(Long studyGroupId, Long requesterMemberId) {
        return new JoinRequestEvent(studyGroupId, requesterMemberId);
    }

    public String getEventId(){
        return this.eventId;
    }
}