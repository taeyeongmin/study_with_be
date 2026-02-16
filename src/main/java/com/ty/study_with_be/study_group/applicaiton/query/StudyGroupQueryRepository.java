package com.ty.study_with_be.study_group.applicaiton.query;

import com.ty.study_with_be.study_group.domain.model.enums.OperationStatus;
import com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import com.ty.study_with_be.study_group.domain.model.enums.StudyRole;
import com.ty.study_with_be.study_group.presentation.query.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StudyGroupQueryRepository {

    boolean existsMember(Long studyGroupId, Long memberId);

    Optional<StudyGroupDetailRes> findDetail(Long studyGroupId, Long currentMemberId);

    Page<StudyGroupListItem> findStudyGroups(String title, String category, String topic, String region, StudyMode studyMode, RecruitStatus recruitStatus, Pageable pageable,Long currentMemberId);

    Optional<StudyRole> findRole(Long groupId, Long memberId);

    boolean hasManagerRole(Long studyGroupId, Long viewerMemberId);

    List<StudyMemberItem> findStudyMemberList(Long studyGroupId);

    List<Long> findManagers(Long studyGroupId);

    Optional<Long> findLeaderId(Long studyGroupId);

    List<Long> findAllMember(Long studyGroupId);

    int countByMemberIdJoined(Long memberId);

    int countByMemberIdOperate(Long memberId);

    Page<MyStudyGroupListItem> findMyStudyGroups(
            Long memberId,
            List<MyStudyGroupOperationFilter> operationStatus,
            List<StudyRole> roleFilter,
            Pageable pageable
    );

    Page<StudyGroupListItem> findPopularStudyGroups(Pageable pageable);

}
