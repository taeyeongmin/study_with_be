package com.ty.study_with_be.study_notice.application.query;

import com.ty.study_with_be.study_notice.presentation.query.dto.StudyNoticeItem;

import java.util.List;

public interface StudyNoticeQueryRepository {

    List<StudyNoticeItem> findRecent(Long groupId, int count);

    List<StudyNoticeItem> findAll(Long groupId);
}
