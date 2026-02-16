package com.ty.study_with_be.notification.presentation.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NotificationListRes {

    private List<NotificationItem> notificationList;
    private int totalCount;
}
