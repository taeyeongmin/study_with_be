package com.ty.study_with_be.study_notice.application.command;

import com.ty.study_with_be.study_notice.presentation.command.dto.StudyNoticeSaveReq;

public interface StudyNoticeCreateUseCase {
    void createNotice(StudyNoticeSaveReq studyNoticeSaveReq, Long currentMemberId);
}
