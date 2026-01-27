package com.ty.study_with_be.study_notice.infra.command;

import com.ty.study_with_be.study_notice.domain.StudyNoticeRepository;
import com.ty.study_with_be.study_notice.domain.model.StudyNotice;
import com.ty.study_with_be.study_notice.infra.StudyNoticeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StudyNoticeRepositoryImpl implements StudyNoticeRepository {

    private final StudyNoticeJpaRepository studyNoticeJpaRepository;

    @Override
    public void save(StudyNotice studyNotice) {

        studyNoticeJpaRepository.save(studyNotice);
    }
}
