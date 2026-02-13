package com.ty.study_with_be.study_group.domain.event;

import com.ty.study_with_be.global.event.domain.DomainEvent;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class RecruitmentResumeEvent extends DomainEvent {

    private final Long studyGroupId;
    private final Long processorMemberId;

    public RecruitmentResumeEvent(Long studyGroupId, Long processorMemberId) {
        this.eventId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.studyGroupId = studyGroupId;
        this.processorMemberId = processorMemberId;
    }

    public static RecruitmentResumeEvent of(Long studyGroupId, Long processorMemberId) {
        return new RecruitmentResumeEvent(studyGroupId, processorMemberId);
    }
}