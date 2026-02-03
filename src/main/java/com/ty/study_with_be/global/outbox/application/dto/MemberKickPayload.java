package com.ty.study_with_be.global.outbox.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberKickPayload {

    private Long studyGroupId;
    private Long processorId;
    private Long targetMemberId;

    public static MemberKickPayload of(Long studyGroupId, Long processorId, Long targetMemberId) {
        MemberKickPayload payload = new MemberKickPayload();
        payload.studyGroupId = studyGroupId;
        payload.processorId = processorId;
        payload.targetMemberId = targetMemberId;
        return payload;
    }
}
