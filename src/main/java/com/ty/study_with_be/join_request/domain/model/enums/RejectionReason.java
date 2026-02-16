package com.ty.study_with_be.join_request.domain.model.enums;

import lombok.Getter;

// 거절 사유
@Getter
public enum RejectionReason {
    MANUAL,
    GROUP_CLOSED
}