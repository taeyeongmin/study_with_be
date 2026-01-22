package com.ty.study_with_be.study_group.domain.model.enums;

public enum StudyRole {
    LEADER,
    MANAGER,
    MEMBER,
    NONE;

    public boolean canManageJoinRequest() {
        return this == LEADER || this == MANAGER;
    }
}