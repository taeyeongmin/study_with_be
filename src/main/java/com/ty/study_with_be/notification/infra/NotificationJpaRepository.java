package com.ty.study_with_be.notification.infra;

import com.ty.study_with_be.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByRecipientMemberIdAndReadAtIsNull(Long recipientMemberId);
}