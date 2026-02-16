package com.ty.study_with_be.global.event.domain;

import lombok.Getter;

@Getter
public enum EventType {
        JOIN_REQUEST("가입 신청")
        , JOIN_CANCEL("가입 취소")
        , JOIN_APPROVE("가입 승인")
        , JOIN_REJECT("가입 반려")
        , MEMBER_LEAVE("회원 탈퇴")
        , NOTICE_CREATE("모임 공지 등록")
        , MEMBER_KICK("회원 강제 퇴장")
        , ROLE_CHANGE("역할 변경")
        , END_RECRUITMENT("모집 종료")
        , RESUME_RECRUITMENT("모집 재개")
        , END_OPERATION("운영 종료");

        private final String eventName;

    EventType(String s) {
        this.eventName = s;
    }
}
