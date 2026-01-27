package com.ty.study_with_be.study_group.applicaiton.command.service;

import com.ty.study_with_be.study_group.applicaiton.command.LeaveGroupUseCase;
import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeaveGroupService implements LeaveGroupUseCase {

    private final GroupRepository groupRepository;

    @Override
    public void leaveGroup(Long studyGroupId, Long memberId) {

        StudyGroup studyGroup = groupRepository.findById(studyGroupId).orElseThrow(() -> new RuntimeException("해당 그룹이 없습니다."));

        studyGroup.leave(memberId);

        groupRepository.save(studyGroup);
    }
}
