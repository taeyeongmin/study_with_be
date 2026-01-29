package com.ty.study_with_be.study_group.applicaiton.command;

import com.ty.study_with_be.study_group.domain.model.enums.StudyRole;

public interface ChangeStudyMemberRoleUseCase {
    void change(Long studyGroupId, Long targetMemberId, Long currentMemberId, StudyRole role);
}
