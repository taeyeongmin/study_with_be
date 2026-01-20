package com.ty.study_with_be.study_group.applicaiton;

import com.ty.study_with_be.study_group.presentation.req.StudyGroupOperationInfoUpdateReq;
import com.ty.study_with_be.study_group.presentation.req.StudyGroupReq;
import jakarta.validation.Valid;

public interface UpdateGroupUseCase {

    void updateAll(StudyGroupReq studyGroupReq, Long memberId, Long groupId);

    void updateOperationInfo(Long studyGroupId, @Valid StudyGroupOperationInfoUpdateReq req);
}
