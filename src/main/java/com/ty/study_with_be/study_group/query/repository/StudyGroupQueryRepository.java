package com.ty.study_with_be.study_group.query.repository;

import com.ty.study_with_be.study_group.query.dto.StudyGroupDetailRes;

import java.util.Optional;

public interface StudyGroupQueryRepository {

    boolean existsMember(Long studyGroupId, Long memberId);

    boolean existsPendingJoin(Long studyGroupId, Long memberId);

    Optional<StudyGroupDetailRes> findDetail(Long studyGroupId);
}
