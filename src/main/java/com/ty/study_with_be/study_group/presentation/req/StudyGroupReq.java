package com.ty.study_with_be.study_group.presentation.req;

import com.ty.study_with_be.study_group.domain.model.enums.SchedulingType;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
public class StudyGroupReq {

    @NotBlank
    @Size(max = 60)
    private String title;

    @NotBlank
    @Size(max = 30)
    private String category;

    @NotBlank
    @Size(max = 60)
    private String topic;

    @NotNull
    private SchedulingType schedulingType;

    List<DayOfWeek> daysOfWeek;   // SCHEDULED일 때만 사용

    /**
     * 진행 요일
     */
    private Set<DayOfWeek> schedules;

    @NotNull
    private StudyMode studyMode;

    @Size(max = 60)
    private String region;

    @Min(1)
    private int capacity;

    @NotBlank
    private String description;

    /**
     * 가입 신청 마감일 (선택)
     * null 허용 → 상시 모집
     */
    private LocalDate applyDeadlineAt;

}