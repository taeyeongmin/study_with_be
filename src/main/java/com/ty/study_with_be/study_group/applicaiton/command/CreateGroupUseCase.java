package com.ty.study_with_be.study_group.applicaiton.command;

import com.ty.study_with_be.study_group.presentation.command.dto.StudyGroupReq;

public interface CreateGroupUseCase {
    void create(StudyGroupReq studyGroupReq, Long memberId);
}
