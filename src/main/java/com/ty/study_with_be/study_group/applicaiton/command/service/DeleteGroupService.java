package com.ty.study_with_be.study_group.applicaiton.command.service;

import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.study_group.applicaiton.command.DeleteGroupUseCase;
import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteGroupService implements DeleteGroupUseCase {

    private final GroupRepository groupRepository;

    @Override
    public void deleteGroup(Long studyGroupId, Long loginMemberId) {

        StudyGroup studyGroup = groupRepository.findById(studyGroupId).orElseThrow(() -> new RuntimeException("해당 그룹이 없습니다."));

        if (!studyGroup.isOwner(loginMemberId))
            throw new DomainException(ErrorCode.NOT_GROUP_OWNER);

        studyGroup.validDelete();

        groupRepository.delete(studyGroup);
    }
}
