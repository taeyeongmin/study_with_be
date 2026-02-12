package com.ty.study_with_be.study_group.presentation.query.dto;

import com.ty.study_with_be.study_group.domain.model.enums.StudyRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "내가 참여 중인 스터디 그룹 목록 조회 요청")
public class MyStudyGroupListReq {

    @Schema(
            description = "운영 상태 필터 (ONGOING / CLOSED)",
            type = "array",
            example = "[\"ONGOING\"]",
            defaultValue = "[\"ONGOING\"]"
    )
    private List<MyStudyGroupOperationFilter> operationFilter =
            List.of(MyStudyGroupOperationFilter.ONGOING);

    @Schema(
            description = "내 역할 필터 (LEADER / MANAGER / MEMBER)",
            type = "array",
            example = "[\"LEADER\"]",
            defaultValue = "[\"LEADER\", \"MANAGER\", \"MEMBER\"]"
    )
    private List<StudyRole> roleFilter = List.of(StudyRole.LEADER, StudyRole.MANAGER, StudyRole.MEMBER);

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
