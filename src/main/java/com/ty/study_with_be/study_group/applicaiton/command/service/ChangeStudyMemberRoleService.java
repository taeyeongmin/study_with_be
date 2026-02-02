package com.ty.study_with_be.study_group.applicaiton.command.service;

import com.ty.study_with_be.global.event.domain.DomainEvent;
import com.ty.study_with_be.study_group.applicaiton.command.ChangeStudyMemberRoleUseCase;
import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import com.ty.study_with_be.study_group.domain.model.enums.StudyRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangeStudyMemberRoleService implements ChangeStudyMemberRoleUseCase {

    private final GroupRepository groupRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void change(Long studyGroupId, Long targetStudyMemberId, Long currentMemberId,StudyRole role) {

        StudyGroup studyGroup = groupRepository.findById(studyGroupId).orElseThrow(() -> new RuntimeException("해당 그룹이 없습니다."));

        studyGroup.changeRole(targetStudyMemberId, currentMemberId, role);

        for (DomainEvent e : studyGroup.pullDomainEvents()) {
            eventPublisher.publishEvent(e);
        }
    }
}
