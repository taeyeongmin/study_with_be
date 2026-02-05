package com.ty.study_with_be.notification.application.command.handler;

public record NotificationCreatedEvent(Long notificationId, Long recipientMemberId) {}