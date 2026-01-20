package com.ty.study_with_be.study_group.presentation.req;

import com.ty.study_with_be.study_group.domain.model.enums.SchedulingType;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Schema(description = "스터디그룹 생성/수정 요청")
public class StudyGroupReq {

    @NotBlank
    @Size(max = 60)
    @Schema(description = "스터디 제목", example = "자바 스터디")
    private String title;

    @NotBlank
    @Size(max = 30)
    @Schema(description = "카테고리", example = "개발")
    private String category;

    @NotBlank
    @Size(max = 60)
    @Schema(description = "주제", example = "스프링 부트")
    private String topic;

    @NotNull
    @Schema(description = "운영 일정 유형", example = "SCHEDULED", implementation = SchedulingType.class)
    private SchedulingType schedulingType;

    /**
     * 진행 요일
     */
    @Schema(description = "운영 요일(스케줄)", example = "[\"MONDAY\",\"WEDNESDAY\"]")
    private Set<DayOfWeek> schedules; // SCHEDULED인 경우만 사용

    @NotNull
    @Schema(description = "스터디 모드", example = "ONLINE", implementation = StudyMode.class)
    private StudyMode studyMode;

    @Size(max = 60)
    @Schema(description = "지역(오프라인일 때 필수)", example = "서울")
    private String region;

    @Min(1)
    @Schema(description = "정원", example = "4")
    private int capacity;

    @NotBlank
    @Schema(description = "설명", example = "주 2회 온라인 스터디입니다.")
    private String description;

    /**
     * 모집 신청 마감일(옵션)
     * null이면 상시 모집
     */
    @Schema(description = "모집 마감일(없으면 상시 모집)", example = "2025-12-31")
    private LocalDate applyDeadlineAt;

}
