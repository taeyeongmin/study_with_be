package com.ty.study_with_be.notification.application.strategy.recipient;

import com.ty.study_with_be.notification.application.strategy.NotificationContext;

import java.util.List;

public interface RecipientStrategy {

    RecipientType getType();
    List<Long> resolveTargetMemberId(NotificationContext context);
}
