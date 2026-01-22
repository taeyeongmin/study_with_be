package com.ty.study_with_be.join_request.presentation.query.dto;

import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class JoinRequestListItem {

    private Long joinRequestId;

    private Long requesterId;
    private String requesterLoginId;
    private String requesterNickname;

    private JoinRequestStatus status;
    private String statusNm;

    private LocalDateTime createdAt;
}