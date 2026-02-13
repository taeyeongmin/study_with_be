package com.ty.study_with_be.notification.application.command.message;

import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.join_request.domain.model.enums.RejectionReason;
import com.ty.study_with_be.notification.application.command.strategy.NotificationContext;
import com.ty.study_with_be.notification.application.command.strategy.recipient.RecipientType;
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

    JOIN_CANCEL(EventType.JOIN_CANCEL,
            "[%s]님이 스터디 가입 요청을 취소 했어요.",
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

    JOIN_REJECTED_MANUAL(
            EventType.JOIN_REJECT,
            "[%s]님이 [%s]의 가입 요청을 거절했어요.",
            "/study-groups/%d",
            new ParamSource[]{ParamSource.PROCESSOR, ParamSource.REQUESTER}),

    JOIN_REJECTED_BY_GROUP_CLOSED(
            EventType.JOIN_REJECT,
            "스터디 운영 종료로 인해 가입 요청이 자동 거절되었어요.",
            "/study-groups/%d",
            new ParamSource[]{}),

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
            new ParamSource[]{ParamSource.PROCESSOR}),

    // 그룹원 모두: 모집 종료
    END_RECRUITMENT(EventType.END_RECRUITMENT,
            "[%s]님이 모집을 종료했어요.",
            "/study-groups/%d/notices",
            new ParamSource[]{ParamSource.PROCESSOR}),

    // 그룹원 모두: 모집 종료
    RESUME_RECRUITMENT(EventType.RESUME_RECRUITMENT,
            "[%s]님이 모집을 재개했어요.",
            "/study-groups/%d/notices",
            new ParamSource[]{ParamSource.PROCESSOR});

    private final EventType eventType;
    private final String messageTemplate;
    private final String linkTemplate;
    private final ParamSource[] paramSources;

    public static NotificationMessageTemplate from(NotificationContext context) {

        EventType eventType = context.getEventType();

        if (eventType == EventType.JOIN_REJECT) {

            if (context.getRejectionReason() == RejectionReason.GROUP_CLOSED) {
                return JOIN_REJECTED_BY_GROUP_CLOSED;
            }

            return JOIN_REJECTED_MANUAL;
        }

        return Arrays.stream(values())
                .filter(v -> v.eventType == eventType)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("해당 타입이 존재하지 않습니다."));
    }
}
