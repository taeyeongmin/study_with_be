package com.ty.study_with_be.study_group.presentation.command.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LeaderLeaveReq {

    @NotNull
    private Long studyMemberId;
}
