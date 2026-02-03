package com.ty.study_with_be.global.outbox.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinProcessPayload {

    private Long studyGroupId;
    private Long requesterMemberId;
    private Long processorId;

    public static JoinProcessPayload of(Long studyGroupId, Long requesterMemberId, Long processorId) {
        JoinProcessPayload payload = new JoinProcessPayload();
        payload.studyGroupId = studyGroupId;
        payload.requesterMemberId = requesterMemberId;
        payload.processorId = processorId;
        return payload;
    }
}
