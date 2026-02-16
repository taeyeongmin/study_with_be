package com.ty.study_with_be.notification.application.command.usecase;

public interface NotificationReadUseCase {
    void read(Long memberId, Long notificationId);
}
