package com.ty.study_with_be.notification.presentation.query.dto;

import com.ty.study_with_be.global.entity.PagingResEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MyNotificationListRes extends PagingResEntity {

    private List<NotificationItem> items;

    public MyNotificationListRes(List<NotificationItem> items, int page, int size, long totalElements, int totalPages, boolean hasNext) {
        super(page, size, totalElements, totalPages, hasNext);
        this.items = items;
    }
}
