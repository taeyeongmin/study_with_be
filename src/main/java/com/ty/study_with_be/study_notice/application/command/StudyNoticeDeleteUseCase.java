package com.ty.study_with_be.study_notice.application.command;

import com.ty.study_with_be.study_notice.presentation.command.dto.StudyNoticeSaveReq;

public interface StudyNoticeDeleteUseCase {
    void deleteNotice(Long studyGroupId, Long noticeId, Long memberId);
}
