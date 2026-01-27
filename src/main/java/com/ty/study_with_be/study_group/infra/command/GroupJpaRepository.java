package com.ty.study_with_be.study_group.infra.command;

import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import com.ty.study_with_be.study_group.domain.model.enums.OperationStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface GroupJpaRepository extends JpaRepository<StudyGroup,Long> {

    int countByOwnerIdAndOperationStatusIn(Long ownerId, Collection<OperationStatus> statuses);

    boolean existsByOwnerIdAndTitleAndOperationStatusIn(Long ownerId, String title, Collection<OperationStatus> statuses);

    boolean existsByOwnerIdAndTitleAndOperationStatusInAndStudyGroupIdNot(Long ownerId, String title, Collection<OperationStatus> operationStatuses, Long studyGroupId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select sg from StudyGroup sg where sg.studyGroupId = :studyGroupId")
    Optional<StudyGroup> findByStudyGroupIdForUpdate(Long studyGroupId);
}
