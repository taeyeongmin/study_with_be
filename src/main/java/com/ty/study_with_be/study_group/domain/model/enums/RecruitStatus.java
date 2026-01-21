package com.ty.study_with_be.study_group.domain.model.enums;

import lombok.Getter;


@Getter
public enum RecruitStatus {
    RECRUITING("모집중"),
    RECRUIT_END("모집종료");
    
    private final String codeNm;

    RecruitStatus(String codeNm) {
        this.codeNm = codeNm;
    }
}