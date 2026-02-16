package com.ty.study_with_be.study_group.presentation.command.dto;

import com.ty.study_with_be.study_group.domain.model.enums.StudyRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class StudyMemberRoleChangeReq {

    @NotNull
    private StudyRole role;
}