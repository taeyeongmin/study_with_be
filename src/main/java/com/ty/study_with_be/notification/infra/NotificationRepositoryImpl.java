package com.ty.study_with_be.notification.infra;

import com.ty.study_with_be.notification.domain.Notification;
import com.ty.study_with_be.notification.domain.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public List<Notification> findNotReadByMemberId(Long memberId) {

        return notificationJpaRepository.findAllByRecipientMemberIdAndReadAtIsNull(memberId);
    }

    @Override
    public void saveAll(List<Notification> notificationList){
        notificationJpaRepository.saveAll(notificationList);
    }

    @Override
    public void save(Notification notification){
        notificationJpaRepository.save(notification);
    }

    @Override
    public Optional<Notification> findById(Long notificationId) {
        return notificationJpaRepository.findById(notificationId);
    }

}
