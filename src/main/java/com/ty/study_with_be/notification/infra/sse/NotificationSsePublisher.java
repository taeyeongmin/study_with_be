package com.ty.study_with_be.notification.infra.sse;

import com.ty.study_with_be.notification.presentation.command.dto.SseNotificationDto;
import org.springframework.stereotype.Component;

@Component
public class NotificationSsePublisher {

    private final SseEmitterRegistry registry;

    public NotificationSsePublisher(SseEmitterRegistry registry) {
        this.registry = registry;
    }

    public void publish(Long recipientMemberId, SseNotificationDto dto) {
        registry.sendToMember(
                recipientMemberId,
                "notification",
                dto
        );
    }
}