package com.ty.study_with_be.notification.application.query;

import com.ty.study_with_be.notification.presentation.query.dto.NotificationItem;

import java.util.List;

public interface NotificationQueryRepository {

    int countNotReadByMemberId(Long memberId);

    List<NotificationItem> findNotReadByMemberId(Long memberId);
}
