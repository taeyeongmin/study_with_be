package com.ty.study_with_be.study_group.domain.model.enums;

import lombok.Getter;

@Getter
public enum JoinRequestStatus {
    PENDING("대기중"),
    APPROVED("승인"),
    REJECTED("거절"),
    CANCELED("취소");

    private final String codeNm;

    JoinRequestStatus(String codeNm) {
        this.codeNm = codeNm;
    }
}