package com.ty.study_with_be.study_group.presentation.query.dto;

import com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "내가 참여 중인 스터디 그룹 목록 조회 요청")
public class StudyGroupListReq {

    @Schema(description = "스터디명")
    private String title;
    
    @Schema(description = "카테고리 코드")
    private String category;

    @Schema(description = "주제")
    private String topic;
    
    @Schema(description = "지역 코드")
    private String region;

    @Schema(
            description = "스터디 진행 방식 (ONLINE/OFFLINE)",
            example = "ONLINE"
    )
    private StudyMode studyMode = StudyMode.ONLINE;

    @Schema(
            description = "모집 상태(RECRUITING/RECRUIT_END)",
            example = "RECRUITING",
            defaultValue = "RECRUITING"
    )
    private RecruitStatus recruitStatus = RecruitStatus.RECRUITING;

    @Schema(
            description = "페이지 번호 (0부터 시작)",
            example = "0",
            defaultValue = "0"
    )
    @Min(0)
    private int page = 0;

    @Schema(
            description = "페이지 크기",
            example = "12",
            defaultValue = "12"
    )
    @Min(1)
    private int size = 20;
}
