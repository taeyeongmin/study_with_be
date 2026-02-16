package com.ty.study_with_be.member.presentation.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashBoardSummaryRes {

    @Schema(description = "참여 그룹 갯수")
    private Integer joinedCount;
    @Schema(description = "운영 그룹 갯수")
    private Integer operatingCount;
    @Schema(description = "가입 신청 요청 갯수")
    private Integer joinRequestCount;
}
