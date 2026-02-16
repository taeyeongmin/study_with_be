package com.ty.study_with_be.notification.application.command.usecase;

import com.ty.study_with_be.notification.domain.Notification;
import com.ty.study_with_be.notification.domain.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationReadService implements NotificationReadUseCase {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void read(Long memberId, Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId).orElseThrow(()-> new IllegalArgumentException("해당 알림이 존재하지 않습니다."));
        notification.markAsRead(memberId);

        notificationRepository.save(notification);
    }
}
