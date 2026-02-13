package com.ty.study_with_be.notification.application.listener;

import com.ty.study_with_be.notification.application.command.handler.NotificationCreatedEvent;
import com.ty.study_with_be.notification.domain.Notification;
import com.ty.study_with_be.notification.infra.NotificationJpaRepository;
import com.ty.study_with_be.notification.infra.sse.NotificationSsePublisher;
import com.ty.study_with_be.notification.presentation.command.dto.SseNotificationDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Notification이 성공적으로 커밋된 후 실행되는 객체
 * - SSE 메세지를 FE로 발생
 */
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