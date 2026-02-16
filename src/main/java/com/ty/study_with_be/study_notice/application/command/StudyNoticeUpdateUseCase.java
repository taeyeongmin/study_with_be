package com.ty.study_with_be.study_notice.application.command;

import com.ty.study_with_be.study_notice.presentation.command.dto.StudyNoticeSaveReq;

public interface StudyNoticeUpdateUseCase {
    void updateNotice(Long studyGroupId, Long noticeId, StudyNoticeSaveReq studyNoticeSaveReq, Long memberId);
}
