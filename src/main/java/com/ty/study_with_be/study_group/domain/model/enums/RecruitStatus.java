package com.ty.study_with_be.study_group.domain.model.enums;

import lombok.Getter;


/**
 * 방장이 변경할 수 있는 모집 상태로 최종 판단은 RecruitAvailability가 수행
 */
@Getter
public enum RecruitStatus {
    RECRUITING("모집중"),
    RECRUIT_END("모집마감");
    
    private final String codeNm;

    RecruitStatus(String codeNm) {
        this.codeNm = codeNm;
    }
}