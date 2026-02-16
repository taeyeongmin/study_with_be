package com.ty.study_with_be.study_group.domain.model.enums;

/**
 * 스터디 모집 상태 판단
 */
public enum RecruitAvailability {
    OPEN,               // 모집중
    CLOSED_MANUAL,      // RecruitStatus 가 RECRUIT_END
    CLOSED_DEADLINE,    // 마감일 
    CLOSED_FULL;         // 정원 초과
}