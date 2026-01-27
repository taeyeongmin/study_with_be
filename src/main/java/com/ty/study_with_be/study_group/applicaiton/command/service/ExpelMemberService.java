package com.ty.study_with_be.study_group.applicaiton.command.service;

import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.study_group.applicaiton.command.ExpelMemberUseCase;
import com.ty.study_with_be.study_group.applicaiton.command.LeaveGroupUseCase;
import com.ty.study_with_be.study_group.applicaiton.query.StudyGroupQueryRepository;
import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpelMemberService implements ExpelMemberUseCase {

    private final GroupRepository groupRepository;
    private final StudyGroupQueryRepository studyGroupQueryRepository;

    @Override
    public void expelMember(Long studyGroupId,Long targetMemberId, Long loginMemberId) {

        // 권한 체크
        if (!studyGroupQueryRepository.hasManagerRole(studyGroupId, loginMemberId)) {
            throw new DomainException(ErrorCode.STUDY_GROUP_NOT_MANAGER);
        }

        StudyGroup studyGroup = groupRepository.findById(studyGroupId).orElseThrow(() -> new RuntimeException("해당 그룹이 없습니다."));

        studyGroup.expelMember(targetMemberId);

        groupRepository.save(studyGroup);
    }
}
