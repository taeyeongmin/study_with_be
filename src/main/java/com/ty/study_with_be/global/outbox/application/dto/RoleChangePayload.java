package com.ty.study_with_be.global.outbox.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoleChangePayload {

    private Long studyGroupId;
    private Long processorId;
    private Long targetMemberId;

    public static RoleChangePayload of(Long studyGroupId, Long processorId, Long targetMemberId) {
        RoleChangePayload payload = new RoleChangePayload();
        payload.studyGroupId = studyGroupId;
        payload.processorId = processorId;
        payload.targetMemberId = targetMemberId;
        return payload;
    }
}
