package com.ty.study_with_be.study_notice.domain;

import com.ty.study_with_be.study_notice.domain.model.StudyNotice;

public interface StudyNoticeRepository {
    void save(StudyNotice studyNotice);
}
