package com.ty.study_with_be.notification.domain;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {

    List<Notification> findNotReadByMemberId(Long memberId);

    void saveAll(List<Notification> notificationList);

    void save(Notification notification);

    Optional<Notification> findById(Long notificationId);
}
