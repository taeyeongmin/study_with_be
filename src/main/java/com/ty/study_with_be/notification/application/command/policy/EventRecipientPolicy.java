package com.ty.study_with_be.notification.application.command.policy;

import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.join_request.domain.model.enums.RejectionReason;
import com.ty.study_with_be.notification.application.command.strategy.NotificationContext;
import com.ty.study_with_be.notification.application.command.strategy.recipient.RecipientType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

/**
 * 이벤트 타입별로 알림 발송 대상에 대한 정책을 관리
 */
@Component
public class EventRecipientPolicy {

    private final Map<EventType, RecipientType> mapping;

    public EventRecipientPolicy() {
        this.mapping = new EnumMap<>(EventType.class);

        mapping.put(EventType.JOIN_REQUEST, RecipientType.LEADER_AND_MANAGER);
        mapping.put(EventType.JOIN_CANCEL, RecipientType.LEADER_AND_MANAGER);
        mapping.put(EventType.JOIN_APPROVE, RecipientType.LEADER_AND_MANAGER_AND_TARGET);

        mapping.put(EventType.MEMBER_LEAVE, RecipientType.LEADER_AND_MANAGER);
        mapping.put(EventType.MEMBER_KICK, RecipientType.LEADER_AND_MANAGER_AND_TARGET);
        mapping.put(EventType.ROLE_CHANGE, RecipientType.LEADER_AND_MANAGER_AND_TARGET);

        mapping.put(EventType.NOTICE_CREATE, RecipientType.ALL_MEMBERS);
        mapping.put(EventType.END_RECRUITMENT, RecipientType.ALL_MEMBERS);
        mapping.put(EventType.RESUME_RECRUITMENT, RecipientType.ALL_MEMBERS);
        mapping.put(EventType.END_OPERATION, RecipientType.ALL_MEMBERS);
    }

    public RecipientType resolve(NotificationContext context) {

        EventType eventType = context.getEventType();

        if (eventType == EventType.JOIN_REJECT) {

            if (context.getRejectionReason() == RejectionReason.GROUP_CLOSED) {
                return RecipientType.TARGET_ONLY;
            }

            return RecipientType.LEADER_AND_MANAGER_AND_TARGET;
        }

        return mapping.get(eventType);
    }
}