package com.ty.study_with_be.study_group.domain.model.enums;

import lombok.Getter;

@Getter
public enum OperationStatus {
    PREPARING("준비중")
    , ONGOING("진행중")
    , CLOSED("종료");

    private final String codeNm;

    OperationStatus(String codeNm) {
        this.codeNm = codeNm;
    }
}
