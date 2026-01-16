package com.ty.study_with_be.study_group.infra;

import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import com.ty.study_with_be.study_group.domain.model.enums.StudyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface GroupJpaRepository extends JpaRepository<StudyGroup,Integer> {

    int countByOwnerIdAndStatusIn(Long ownerId, Collection<StudyStatus> statuses);


    boolean existsByOwnerIdAndTitleAndStatusIn(Long ownerId, String title, Collection<StudyStatus> statuses);
}
