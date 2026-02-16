package com.ty.study_with_be.study_notice.application.command.service;

import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.study_group.applicaiton.query.StudyGroupQueryRepository;
import com.ty.study_with_be.study_notice.application.command.StudyNoticeDeleteUseCase;
import com.ty.study_with_be.study_notice.domain.StudyNoticeRepository;
import com.ty.study_with_be.study_notice.domain.model.StudyNotice;
import com.ty.study_with_be.study_notice.presentation.command.dto.StudyNoticeSaveReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyNoticeDeleteService implements StudyNoticeDeleteUseCase {

    private final StudyGroupQueryRepository studyGroupQueryRepository;
    private final StudyNoticeRepository studyNoticeRepository;

    @Override
    @Transactional
    public void deleteNotice(Long studyGroupId, Long noticeId, Long memberId) {

        // 권한 체크
        if (!studyGroupQueryRepository.hasManagerRole(studyGroupId, memberId))
            throw new DomainException(ErrorCode.STUDY_GROUP_NOT_MANAGER);

        // Notice Entity 조회
        StudyNotice studyNotice = studyNoticeRepository.findById(noticeId).orElseThrow(() -> new RuntimeException("해당 공지가 존재하지 않습니다."));


        studyNoticeRepository.delete(studyNotice);
    }

}
