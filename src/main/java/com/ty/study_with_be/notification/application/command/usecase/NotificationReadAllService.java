package com.ty.study_with_be.notification.application.command.usecase;

import com.ty.study_with_be.notification.domain.Notification;
import com.ty.study_with_be.notification.domain.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationReadAllService implements NotificationReadAllUseCase {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void readAll(Long memberId) {

        List<Notification> notificationList = notificationRepository.findNotReadByMemberId(memberId);
        notificationList.forEach(noti -> noti.markAsRead(memberId));

        notificationRepository.saveAll(notificationList);
    }
}
