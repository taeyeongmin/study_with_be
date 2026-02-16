package com.ty.study_with_be.notification.application;

public record NotificationCreatedEvent(Long notificationId, Long recipientMemberId) {}