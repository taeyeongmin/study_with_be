package com.ty.study_with_be.notification.application.message;

import com.ty.study_with_be.member.application.query.MemberQueryRepository;
import com.ty.study_with_be.notification.application.strategy.NotificationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 알림 메세지 생성을 책임
 * - 대상들의 닉네임 조회
 * - 타입별 메세지 템플릿 조회 
 * - 두 개를 조합하여 최종 메세지 생성
 */
@Component
@RequiredArgsConstructor
public class NotificationMessageComposer {

    private final MemberQueryRepository memberQueryRepository;

    public String createMessage(NotificationContext context) {
        NotificationMessageTemplate template = NotificationMessageTemplate.from(context.getEventType());
        ParamSource[] sources = template.getParamSources();

        Object[] params = new Object[sources.length];
        for (int i = 0; i < sources.length; i++) {
            Long memberId = resolveMemberId(sources[i], context);
            params[i] = findNickname(memberId);
        }

        return String.format(template.getMessageTemplate(), params);
    }

    private Long resolveMemberId(ParamSource source, NotificationContext context) {
        return switch (source) {
            case PROCESSOR -> context.getProcessMemberId();
            case REQUESTER -> context.getRequesterMemberId();
            case TARGET -> context.getTargetMemberId();
        };
    }

    private String findNickname(Long memberId) {
        if (memberId == null) {
            throw new IllegalArgumentException("Notification message memberId is null.");
        }
        return memberQueryRepository.findNicknameById(memberId)
                .orElse("없음");
    }
}
