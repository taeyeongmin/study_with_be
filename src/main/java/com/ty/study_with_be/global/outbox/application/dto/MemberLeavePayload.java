package com.ty.study_with_be.global.outbox.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberLeavePayload {

    private Long studyGroupId;
    private Long leaveMemberId;

    public static MemberLeavePayload of(Long studyGroupId, Long leaveMemberId) {
        MemberLeavePayload payload = new MemberLeavePayload();
        payload.studyGroupId = studyGroupId;
        payload.leaveMemberId = leaveMemberId;
        return payload;
    }
}
