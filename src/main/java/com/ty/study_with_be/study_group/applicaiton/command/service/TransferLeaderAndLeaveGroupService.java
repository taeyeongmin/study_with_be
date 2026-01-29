package com.ty.study_with_be.study_group.applicaiton.command.service;

import com.ty.study_with_be.study_group.applicaiton.command.TransferLeaderAndLeaveGroupUseCase;
import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import com.ty.study_with_be.study_group.domain.model.enums.StudyRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferLeaderAndLeaveGroupService implements TransferLeaderAndLeaveGroupUseCase {

    private final GroupRepository groupRepository;

    @Override
    @Transactional
    public void leaveGroupLeader(Long studyGroupId, Long currentMemberId, Long targetStudyMemberId) {

        StudyGroup studyGroup = groupRepository.findById(studyGroupId).orElseThrow(() -> new RuntimeException("해당 그룹이 없습니다."));

        studyGroup.transferLeaderAndLeave(targetStudyMemberId, currentMemberId);

        groupRepository.save(studyGroup);
    }
}
