package com.ty.study_with_be.join_request.presentation.query.dto;

import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;

public enum MyRequestStatusFilter {
    PENDING,
    APPROVED,
    REJECTED,
    CANCELED,
    ALL;

    public JoinRequestStatus toStatusOrNull() {
        if (this == ALL) {
            return null;
        }
        return JoinRequestStatus.valueOf(name());
    }
}
