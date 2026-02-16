package com.ty.study_with_be.study_notice.domain;

import com.ty.study_with_be.study_notice.domain.model.StudyNotice;

import java.util.Optional;

public interface StudyNoticeRepository {
    void save(StudyNotice studyNotice);

    Optional<StudyNotice> findById(Long noticeId);

    void delete(StudyNotice studyNotice);
}
