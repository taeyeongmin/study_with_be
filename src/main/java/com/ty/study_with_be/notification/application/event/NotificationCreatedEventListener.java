package com.ty.study_with_be.notification.application.event;

import com.ty.study_with_be.notification.domain.Notification;
import com.ty.study_with_be.notification.presentation.command.dto.SseNotificationDto;
import com.ty.study_with_be.notification.infra.NotificationJpaRepository;
import com.ty.study_with_be.notification.infra.sse.NotificationSsePublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificationCreatedEventListener {

    private final NotificationJpaRepository notificationRepository;
    private final NotificationSsePublisher publisher;

    public NotificationCreatedEventListener(NotificationJpaRepository notificationRepository,
                                           NotificationSsePublisher publisher) {
        this.notificationRepository = notificationRepository;
        this.publisher = publisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(NotificationCreatedEvent event) {
        Notification notification = notificationRepository.findById(event.notificationId())
                .orElse(null);

        if (notification == null) return;

        publisher.publish(event.recipientMemberId(), SseNotificationDto.from(notification));
    }
}