package com.ty.study_with_be.join_request.presentation.query.dto;

import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class JoinRequestProcReq {

    @NotNull
    private JoinRequestStatus status;

}