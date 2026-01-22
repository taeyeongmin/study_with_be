package com.ty.study_with_be.study_group.presentation.query.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "스터디 그룹 목록 아이템")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudyGroupListItem {

    @Schema(description = "스터디 그룹 ID", example = "1")
    private Long studyGroupId;

    @Schema(description = "스터디 그룹 제목", example = "자바 스터디")
    private String title;

    @Schema(description = "카테고리 코드", example = "development")
    private String category;
    @Schema(description = "카테고리명", example = "개발")
    private String categoryNm;
    @Schema(description = "주제", example = "스프링 부트")
    private String topic;

    @Schema(description = "모집 상태", example = "RECRUITING")
    private RecruitStatus recruitStatus;
    @Schema(description = "모집 상태명", example = "모집중")
    private String recruitStatusNm;

    @Schema(description = "설명", example = "주 2회 온라인 스터디입니다.")
    private String description;
    @Schema(description = "정원", example = "6")
    private Integer capacity;
    @Schema(description = "현재 인원", example = "3")
    private Integer currentCount;

    @Getter(AccessLevel.NONE)
    private Integer dDay;

    @Schema(description = "D-Day(마감일까지 남은 일수)", example = "7")
    @JsonProperty("dDay")
    public Integer getDDay() {
        return dDay;
    }
}
