package com.ty.study_with_be.notification.infra;

import com.ty.study_with_be.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
}