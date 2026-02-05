package com.ty.study_with_be.notification.application.command.message;

import com.ty.study_with_be.global.event.domain.EventType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum NotificationMessageTemplate {

    // 방장/매니저: 가입요청, 그룹탈퇴
    JOIN_REQUESTED(EventType.JOIN_REQUEST,
            "[%s]님이 스터디 가입을 요청했어요.",
            "/study-groups/%d",
            new ParamSource[]{ParamSource.REQUESTER}),

    GROUP_LEAVED(EventType.MEMBER_LEAVE,
            "[%s]님이 스터디를 탈퇴했어요.",
            "/study-groups/%d",
            new ParamSource[]{ParamSource.PROCESSOR}),

    // 방장/매니저/대상자: 가입 승인/거절, 강제탈퇴, 역할변경
    JOIN_APPROVED(EventType.JOIN_APPROVE,
            "[%s]님이 [%s]의 가입 요청을 승인했어요.",
            "/study-groups/%d",
            new ParamSource[]{ParamSource.PROCESSOR, ParamSource.REQUESTER}),

    JOIN_REJECTED(EventType.JOIN_REJECT,
            "[%s]님이 [%s]의 가입 요청을 거절했어요.",
            "/study-groups/%d",
            new ParamSource[]{ParamSource.PROCESSOR, ParamSource.REQUESTER}),

    FORCED_LEAVE(EventType.MEMBER_KICK,
            "[%s]님이 [%s]님을 강제 탈퇴시켰어요.",
            "/study-groups/%d",
            new ParamSource[]{ParamSource.PROCESSOR, ParamSource.TARGET}),

    ROLE_CHANGED(EventType.ROLE_CHANGE,
            "[%s]님이 [%s]님의 역할을 변경했어요.",
            "/study-groups/%d",
            new ParamSource[]{ParamSource.PROCESSOR, ParamSource.TARGET}),

    // 그룹원 모두: 공지 등록
    NOTICE_CREATED(EventType.NOTICE_CREATE,
            "[%s]님이 새로운 공지를 등록했어요.",
            "/study-groups/%d/notices",
            new ParamSource[]{ParamSource.PROCESSOR});

    private final EventType eventType;
    private final String messageTemplate; // String.format("%s", actorName) 형태
    private final String linkTemplate;    // String.format("%d", studyGroupId) 형태
    private final ParamSource[] paramSources;

    public static NotificationMessageTemplate from(EventType type) {
        return Arrays.stream(values())
                .filter(v -> v.eventType == type)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("해당 타입이 존재하지 않습니다."));
    }
}
