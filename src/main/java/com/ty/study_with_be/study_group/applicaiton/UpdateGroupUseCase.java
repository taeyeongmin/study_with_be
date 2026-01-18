package com.ty.study_with_be.study_group.applicaiton;

import com.ty.study_with_be.study_group.presentation.req.StudyGroupReq;

public interface UpdateGroupUseCase {
    void update(StudyGroupReq studyGroupReq, Long memberId, Long groupId);
}
