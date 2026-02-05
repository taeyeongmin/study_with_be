package com.ty.study_with_be.notification.application.handler;

public record NotificationCreatedEvent(Long notificationId, Long recipientMemberId) {}