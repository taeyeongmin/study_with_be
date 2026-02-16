package com.ty.study_with_be.global.outbox.application.dto;

import com.ty.study_with_be.join_request.domain.model.enums.RejectionReason;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxPayload {

    // 스터디 그룹 id
    private Long studyGroupId;
    // 처리자
    private Long processMemberId;
    // 대상자
    private Long targetMemberId;
    // 요청자
    private Long requesterMemberId;
    // 거절 사유(거절시만 사용)
    private RejectionReason rejectionReason; 

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

    public static OutboxPayload of(Long studyGroupId,
                                   Long processMemberId,
                                   Long targetMemberId,
                                   Long requesterMemberId,
                                   RejectionReason reason
    ) {
        OutboxPayload payload = of(studyGroupId, processMemberId, targetMemberId, requesterMemberId);
        payload.rejectionReason = reason;
        return payload;
    }
}
