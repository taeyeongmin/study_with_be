package com.ty.study_with_be.study_group.presentation.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.Getter;

@Data
@Schema(description = "내 참여 스터디 그룹 목록 조회 요청")
public class MyStudyGroupListReq {

    @Schema(
            description = "운영 상태 필터 (PREPARING / ONGOING / CLOSED / ALL)",
            example = "ONGOING",
            defaultValue = "ONGOING"
    )
    private MyStudyGroupOperationFilter operationFilter = MyStudyGroupOperationFilter.ONGOING;

    @Schema(
            description = "페이지 번호 (0부터 시작)",
            example = "0",
            defaultValue = "0"
    )
    @Min(0)
    private int page = 0;

    @Schema(
            description = "페이지 크기",
            example = "20",
            defaultValue = "20"
    )
    @Min(1)
    private int size = 20;
}
