package com.ty.study_with_be.study_group.domain;

import com.ty.study_with_be.study_group.domain.model.StudyGroup;

public interface GroupRepository {

    int countActiveByMemberId(Long memberId);

    boolean existActiveByMemberIdAndTitle(Long memberId, String title);

    void save(StudyGroup studyGroup);
}
