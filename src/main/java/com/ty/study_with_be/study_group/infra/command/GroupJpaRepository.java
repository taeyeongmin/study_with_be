package com.ty.study_with_be.study_group.infra.command;

import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import com.ty.study_with_be.study_group.domain.model.enums.OperationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface GroupJpaRepository extends JpaRepository<StudyGroup,Long> {

    int countByOwnerIdAndOperationStatusIn(Long ownerId, Collection<OperationStatus> statuses);

    boolean existsByOwnerIdAndTitleAndOperationStatusIn(Long ownerId, String title, Collection<OperationStatus> statuses);

    boolean existsByOwnerIdAndTitleAndOperationStatusInAndStudyGroupIdNot(Long ownerId, String title, Collection<OperationStatus> operationStatuses, Long studyGroupId);

}
