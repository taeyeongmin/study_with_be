package com.ty.study_with_be.notification.application.event;

public record NotificationCreatedEvent(Long notificationId, Long recipientMemberId) {}