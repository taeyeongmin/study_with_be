package com.ty.study_with_be.notification.application.command.strategy.recipient;

/**
 * 알림 대상에 타입을 관리
 */
public enum RecipientType {
    LEADER_AND_MANAGER,
    LEADER_AND_MANAGER_AND_TARGET,
    ALL_MEMBERS,
    TARGET_ONLY,
}
