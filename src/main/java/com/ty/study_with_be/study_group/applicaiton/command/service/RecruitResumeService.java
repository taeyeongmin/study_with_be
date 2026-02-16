package com.ty.study_with_be.study_group.applicaiton.command.service;

import com.ty.study_with_be.global.event.domain.DomainEvent;
import com.ty.study_with_be.study_group.applicaiton.command.RecruitResumeUseCase;
import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitResumeService implements RecruitResumeUseCase {

    private final GroupRepository groupRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void resumeRecruitment(Long studyGroupId, Long currentMemberId) {

        StudyGroup studyGroup = groupRepository.findById(studyGroupId).orElseThrow(() -> new RuntimeException("해당 그룹이 없습니다."));
        studyGroup.resumeRecruitment(currentMemberId);

        for (DomainEvent e : studyGroup.pullDomainEvents()) {
            eventPublisher.publishEvent(e);
        }
    }
}
