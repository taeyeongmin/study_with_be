package com.ty.study_with_be.study_group.applicaiton.command;

import com.ty.study_with_be.study_group.presentation.command.dto.StudyGroupOperationInfoUpdateReq;
import com.ty.study_with_be.study_group.presentation.command.dto.StudyGroupReq;
import jakarta.validation.Valid;

public interface UpdateGroupUseCase {

    void updateAll(StudyGroupReq studyGroupReq, Long memberId, Long groupId);

    void updateOperationInfo(Long studyGroupId, @Valid StudyGroupOperationInfoUpdateReq req);
}
