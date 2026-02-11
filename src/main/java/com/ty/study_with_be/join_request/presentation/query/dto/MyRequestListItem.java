package com.ty.study_with_be.join_request.presentation.query.dto;

import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyRequestListItem {

    private Long joinRequestId;
    private Long studyGroupId;
    private String studyGroupTitle;

    private JoinRequestStatus status;
    private String statusNm;

    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
}
