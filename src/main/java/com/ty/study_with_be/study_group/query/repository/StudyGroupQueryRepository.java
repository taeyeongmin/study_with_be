package com.ty.study_with_be.study_group.query.repository;

import com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import com.ty.study_with_be.study_group.query.dto.StudyGroupDetailRes;
import com.ty.study_with_be.study_group.query.dto.StudyGroupListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StudyGroupQueryRepository {

    boolean existsMember(Long studyGroupId, Long memberId);

    boolean existsPendingJoin(Long studyGroupId, Long memberId);

    Optional<StudyGroupDetailRes> findDetail(Long studyGroupId);

    Page<StudyGroupListItem> findStudyGroups(String category, String topic, String region, StudyMode studyMode, RecruitStatus recruitStatus, Pageable pageable);

}
