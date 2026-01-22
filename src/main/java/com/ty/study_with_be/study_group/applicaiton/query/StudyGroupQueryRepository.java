package com.ty.study_with_be.study_group.applicaiton.query;

import com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import com.ty.study_with_be.study_group.domain.model.enums.StudyRole;
import com.ty.study_with_be.study_group.presentation.query.dto.StudyGroupDetailRes;
import com.ty.study_with_be.study_group.presentation.query.dto.StudyGroupListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StudyGroupQueryRepository {

    boolean existsMember(Long studyGroupId, Long memberId);

    Optional<StudyGroupDetailRes> findDetail(Long studyGroupId);

    Page<StudyGroupListItem> findStudyGroups(String category, String topic, String region, StudyMode studyMode, RecruitStatus recruitStatus, Pageable pageable);

    Optional<StudyRole> findRole(Long groupId, Long memberId);

    boolean hasManagerRole(Long studyGroupId, Long viewerMemberId);
}
