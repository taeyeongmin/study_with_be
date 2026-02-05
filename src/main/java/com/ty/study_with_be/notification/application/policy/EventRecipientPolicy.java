package com.ty.study_with_be.notification.application.policy;

import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.notification.application.strategy.recipient.RecipientType;
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
        mapping.put(EventType.JOIN_REJECT, RecipientType.LEADER_AND_MANAGER_AND_TARGET);

        mapping.put(EventType.MEMBER_LEAVE, RecipientType.LEADER_AND_MANAGER);
        mapping.put(EventType.MEMBER_KICK, RecipientType.LEADER_AND_MANAGER_AND_TARGET);
        mapping.put(EventType.ROLE_CHANGE, RecipientType.LEADER_AND_MANAGER_AND_TARGET);

        mapping.put(EventType.NOTICE_CREATE, RecipientType.ALL_MEMBERS);
    }

    public RecipientType resolve(EventType eventType) {
        RecipientType type = mapping.get(eventType);
        if (type == null) {
            throw new IllegalStateException("No RecipientType mapping for " + eventType);
        }
        return type;
    }
}