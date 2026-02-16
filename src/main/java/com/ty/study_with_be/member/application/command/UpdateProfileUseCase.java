package com.ty.study_with_be.member.application.command;

import com.ty.study_with_be.member.presentation.command.dto.UpdateProfileReq;

public interface UpdateProfileUseCase {

    void update(Long memberId, UpdateProfileReq req);
}
