package com.ty.study_with_be.study_group.applicaiton.command.service;

import com.ty.study_with_be.study_group.applicaiton.command.UpdateGroupUseCase;
import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.GroupUpdatePolicy;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import com.ty.study_with_be.study_group.presentation.command.dto.StudyGroupOperationInfoUpdateReq;
import com.ty.study_with_be.study_group.presentation.command.dto.StudyGroupReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateGroupService implements UpdateGroupUseCase {

    private final GroupRepository groupRepository;
    private final GroupUpdatePolicy groupUpdatePolicy;

    @Override
    @Transactional
    public void updateAll(StudyGroupReq studyGroupReq, Long memberId, Long studyGroupId) {

        // 기존 Group 정보 조회
        StudyGroup studyGroup = groupRepository.findById(studyGroupId).orElseThrow(() -> new RuntimeException("해당 그룹이 없습니다."));

        // 수정 정책 검증
        groupUpdatePolicy.valid(memberId,studyGroupReq.getTitle(),studyGroupId);

        // 값 변경
        studyGroup.updateInfo(
                studyGroupReq.getTitle()
                , studyGroupReq.getCategory()
                , studyGroupReq.getTopic()
                , studyGroupReq.getRegion()
                , studyGroupReq.getStudyMode()
                , studyGroupReq.getCapacity()
                , studyGroupReq.getDescription()
                , studyGroupReq.getApplyDeadlineAt()
                , studyGroupReq.getSchedules()
        );

        // DB 저장
        groupRepository.save(studyGroup);
    }

    @Override
    public void updateOperationInfo(Long studyGroupId, StudyGroupOperationInfoUpdateReq req) {

        // 기존 Group 정보 조회
        StudyGroup studyGroup = groupRepository.findById(studyGroupId).orElseThrow(() -> new RuntimeException("해당 그룹이 없습니다."));

        // 값 변경
        studyGroup.updateOperationInfo(req.getCapacity(), req.getStudyMode(), req.getSchedulingType(), req.getSchedules());

        // DB 저장
        groupRepository.save(studyGroup);

    }
}
