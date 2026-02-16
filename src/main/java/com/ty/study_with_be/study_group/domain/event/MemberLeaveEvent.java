package com.ty.study_with_be.study_group.domain.event;

import com.ty.study_with_be.global.event.domain.DomainEvent;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class MemberLeaveEvent extends DomainEvent {

    private final Long studyGroupId;
    private final Long leaveMemberId;

    public MemberLeaveEvent(Long studyGroupId, Long leaveMemberId) {
        this.eventId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.studyGroupId = studyGroupId;
        this.leaveMemberId = leaveMemberId;
    }

    public static MemberLeaveEvent of(Long studyGroupId, Long leaveMemberId) {
        return new MemberLeaveEvent(studyGroupId, leaveMemberId);
    }

}