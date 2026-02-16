package com.ty.study_with_be.study_notice.infra.command;

import com.ty.study_with_be.study_notice.domain.StudyNoticeRepository;
import com.ty.study_with_be.study_notice.domain.model.StudyNotice;
import com.ty.study_with_be.study_notice.infra.StudyNoticeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudyNoticeRepositoryImpl implements StudyNoticeRepository {

    private final StudyNoticeJpaRepository studyNoticeJpaRepository;

    @Override
    public void save(StudyNotice studyNotice) {

        studyNoticeJpaRepository.save(studyNotice);
    }

    @Override
    public Optional<StudyNotice> findById(Long noticeId) {

        return studyNoticeJpaRepository.findById(noticeId);
    }

    @Override
    public void delete(StudyNotice studyNotice) {

        studyNoticeJpaRepository.delete(studyNotice);
    }
}
