package com.ty.study_with_be.study_group.domain.event;

import com.ty.study_with_be.global.event.domain.DomainEvent;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class OperationEndEvent extends DomainEvent {

    private final Long studyGroupId;
    private final Long processorMemberId;

    public OperationEndEvent(Long studyGroupId, Long processorMemberId) {
        this.eventId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.studyGroupId = studyGroupId;
        this.processorMemberId = processorMemberId;
    }

    public static OperationEndEvent of(Long studyGroupId, Long processorMemberId) {
        return new OperationEndEvent(studyGroupId, processorMemberId);
    }
}