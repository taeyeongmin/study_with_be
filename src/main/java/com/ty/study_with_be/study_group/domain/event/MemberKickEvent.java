package com.ty.study_with_be.study_group.domain.event;

import com.ty.study_with_be.global.event.domain.DomainEvent;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class MemberKickEvent extends DomainEvent {

    private final Long studyGroupId;
    private final Long targetMemberId;
    private final Long processorMemberId;

    public MemberKickEvent(Long studyGroupId, Long leaveMemberId, Long processorMemberId) {
        this.eventId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.studyGroupId = studyGroupId;
        this.targetMemberId = leaveMemberId;
        this.processorMemberId = processorMemberId;
    }

    public static MemberKickEvent of(Long studyGroupId, Long leaveMemberId, Long processorMemberId) {
        return new MemberKickEvent(studyGroupId, leaveMemberId, processorMemberId);
    }

    public String getEventId(){
        return this.eventId;
    }
}