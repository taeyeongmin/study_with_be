package com.ty.study_with_be.notification.application.query;

import com.ty.study_with_be.notification.presentation.query.dto.NotificationItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationQueryRepository {

    int countNotReadByMemberId(Long memberId);

    List<NotificationItem> findNotReadByMemberId(Long memberId);

    Page<NotificationItem> findMyNotifications(Long memberId, Boolean unreadOnly, Pageable pageable);
}
