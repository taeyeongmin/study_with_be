package com.ty.study_with_be.global.outbox.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OutboxPayload {

    // 스터디 그룹 id
    private Long studyGroupId;
    // 처리자
    private Long processMemberId;
    // 대상자
    private Long targetMemberId;
    // 요청자
    private Long requesterMemberId;

    public static OutboxPayload of(Long studyGroupId,
                                   Long processMemberId,
                                   Long targetMemberId,
                                   Long requesterMemberId) {
        OutboxPayload payload = new OutboxPayload();
        payload.studyGroupId = studyGroupId;
        payload.processMemberId = processMemberId;
        payload.targetMemberId = targetMemberId;
        payload.requesterMemberId = requesterMemberId;
        return payload;
    }
}
