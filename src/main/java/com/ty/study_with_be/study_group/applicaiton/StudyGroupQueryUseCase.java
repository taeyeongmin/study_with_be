package com.ty.study_with_be.study_group.applicaiton;

import com.ty.study_with_be.study_group.presentation.req.StudyGroupDetailRes;

public interface StudyGroupQueryUseCase {
    StudyGroupDetailRes getDetail(Long groupId);
}
