package com.ty.study_with_be.study_notice.infra;


import com.ty.study_with_be.study_notice.domain.model.StudyNotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyNoticeJpaRepository extends JpaRepository<StudyNotice,Long> {

}
