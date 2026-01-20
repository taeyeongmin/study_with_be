package com.ty.study_with_be.study_group.presentation.command.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.ty.study_with_be.study_group.domain.model.enums.SchedulingType;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.DayOfWeek;
import java.util.Set;

@Data
@Schema(description = "스터디그룹 운영정보 수정 요청")
public class StudyGroupOperationInfoUpdateReq {

    @Min(1)
    @Schema(description = "정원", example = "4")
    private int capacity;
    @NotNull
    @Schema(description = "스터디 모드", example = "ONLINE", implementation = StudyMode.class)
    private StudyMode studyMode;
    @NotNull
    @Schema(description = "운영 일정 유형", example = "SCHEDULED", implementation =  SchedulingType.class)
    private SchedulingType schedulingType;

    @Schema(description = "운영 요일(스케줄)", example = "[\"MONDAY\",\"WEDNESDAY\"]")
    private Set<DayOfWeek> schedules;

    @JsonAnySetter
    public void failOnUnknown(String key, Object value) {
        throw new IllegalArgumentException("Unknown field: " + key);
    }
}
