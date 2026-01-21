package com.ty.study_with_be.study_group.domain;

import com.ty.study_with_be.study_group.domain.model.StudyGroup;

import java.util.Optional;

public interface GroupRepository {

    int countActiveByMemberId(Long memberId);

    boolean existActiveByMemberIdAndTitle(Long memberId, String title);

    boolean existsActiveByMemberIdAndTitleExcludingGroupId(Long memberId, String title, Long groupId);

    void save(StudyGroup studyGroup);

    Optional<StudyGroup> findById(Long studyGroupId);

    void delete(StudyGroup studyGroup);
}
