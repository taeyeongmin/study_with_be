package com.ty.study_with_be.notification.presentation.command.dto;

import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.notification.domain.Notification;

import java.time.LocalDateTime;

public record SseNotificationDto(
        Long id,
        EventType type,
        String title,
        String message,
        boolean read,
        LocalDateTime createdAt
) {
    public static SseNotificationDto from(Notification notification) {
        return new SseNotificationDto(
                notification.getId(),
                notification.getType(),
                notification.getType().getEventName(),
                notification.getContent(),
                notification.getReadAt() != null,
                notification.getCreatedAt()
        );
    }
}
