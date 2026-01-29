package com.ty.study_with_be.study_group.applicaiton.command.service;

import com.ty.study_with_be.study_group.applicaiton.command.ExpelMemberUseCase;
import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpelMemberService implements ExpelMemberUseCase {

    private final GroupRepository groupRepository;

    @Override
    public void expelMember(Long studyGroupId,Long targetMemberId, Long loginMemberId) {

        StudyGroup studyGroup = groupRepository.findById(studyGroupId).orElseThrow(() -> new RuntimeException("해당 그룹이 없습니다."));

        studyGroup.expelMember(targetMemberId,loginMemberId);

        groupRepository.save(studyGroup);
    }
}
