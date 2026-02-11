package com.ty.study_with_be.join_request.presentation.query.dto;

import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyRequestListItem {

    @Schema(description = "가입 신청 ID")
    private Long joinRequestId;
    @Schema(description = "스터디 그룹 ID")
    private Long studyGroupId;
    @Schema(description = "스터디 그룹명")
    private String studyGroupTitle;
    @Schema(description = "신청 상태")
    private JoinRequestStatus status;
    @Schema(description = "신청 상태명")
    private String statusNm;
    @Schema(description = "신청일")
    private LocalDateTime requestedAt;
    @Schema(description = "처리일")
    private LocalDateTime processedAt;
}
