package com.ty.study_with_be.global.outbox.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinRequestPayload {

    private Long studyGroupId;
    private Long requesterMemberId;

    public static JoinRequestPayload of(Long studyGroupId, Long requesterMemberId) {
        JoinRequestPayload payload = new JoinRequestPayload();
        payload.studyGroupId = studyGroupId;
        payload.requesterMemberId = requesterMemberId;
        return payload;
    }
}
