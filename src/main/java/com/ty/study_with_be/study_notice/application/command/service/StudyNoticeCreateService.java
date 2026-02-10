package com.ty.study_with_be.study_notice.application.command.service;

import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.event.domain.DomainEvent;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.study_group.applicaiton.query.StudyGroupQueryRepository;
import com.ty.study_with_be.study_notice.application.command.StudyNoticeCreateUseCase;
import com.ty.study_with_be.study_notice.domain.StudyNoticeRepository;
import com.ty.study_with_be.study_notice.domain.model.StudyNotice;
import com.ty.study_with_be.study_notice.presentation.command.dto.StudyNoticeSaveReq;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyNoticeCreateService implements StudyNoticeCreateUseCase {

    private final StudyGroupQueryRepository studyGroupQueryRepository;
    private final StudyNoticeRepository studyNoticeRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void createNotice(StudyNoticeSaveReq req, Long currentMemberId) {

        // 권한 체크
        if (!studyGroupQueryRepository.hasManagerRole(req.getStudyGroupId(), currentMemberId)) {
            throw new DomainException(ErrorCode.STUDY_GROUP_NOT_MANAGER);
        }

        StudyNotice studyNotice = StudyNotice.create(
                req.getStudyGroupId()
                , currentMemberId
                , req.getTitle()
                , req.getContent()
                , req.getPinned()
        );

        studyNoticeRepository.save(studyNotice);

        for (DomainEvent e : studyNotice.pullDomainEvents()) {
            eventPublisher.publishEvent(e);
        }
    }
}
