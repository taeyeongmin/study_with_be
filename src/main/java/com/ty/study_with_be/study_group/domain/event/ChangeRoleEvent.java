package com.ty.study_with_be.study_group.domain.event;

import com.ty.study_with_be.global.event.domain.DomainEvent;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ChangeRoleEvent extends DomainEvent {

    private final Long studyGroupId;
    private final Long targetMemberId;
    private final Long processorMemberId;

    public ChangeRoleEvent(Long studyGroupId, Long targetMemberId, Long processorMemberId) {
        this.eventId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.studyGroupId = studyGroupId;
        this.targetMemberId = targetMemberId;
        this.processorMemberId = processorMemberId;
    }

    public static ChangeRoleEvent of(Long studyGroupId, Long targetMemberId, Long processorMemberId) {
        return new ChangeRoleEvent(studyGroupId, targetMemberId, processorMemberId);
    }

    public String getEventId(){
        return this.eventId;
    }
}