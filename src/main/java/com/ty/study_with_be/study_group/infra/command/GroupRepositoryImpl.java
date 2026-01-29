package com.ty.study_with_be.study_group.infra.command;

import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import com.ty.study_with_be.study_group.domain.model.enums.OperationStatus;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GroupRepositoryImpl implements GroupRepository {

    private final GroupJpaRepository groupJpaRepository;

    private static final List<OperationStatus> ACTIVE_STATUSES =
            List.of(
                    OperationStatus.PREPARING,
                    OperationStatus.ONGOING
            );

    @Override
    public int countActiveByMemberId(Long memberId) {

        return groupJpaRepository.countByOwnerIdAndOperationStatusIn(memberId, ACTIVE_STATUSES);
    }

    @Override
    public boolean existActiveByMemberIdAndTitle(Long memberId,  String title) {

        return groupJpaRepository.existsByOwnerIdAndTitleAndOperationStatusIn(memberId,title,ACTIVE_STATUSES);
    }

    @Override
    public boolean existsActiveByMemberIdAndTitleExcludingGroupId(Long memberId, String title, Long groupId) {

        return groupJpaRepository.existsByOwnerIdAndTitleAndOperationStatusInAndStudyGroupIdNot(memberId,title,ACTIVE_STATUSES,groupId);
    }

    @Override
    public void save(StudyGroup studyGroup) {
        groupJpaRepository.save(studyGroup);
    }

    @Override
    public Optional<StudyGroup> findById(Long studyGroupId) {
        System.out.println(">>>>>>>>>> findById 호출");
        return groupJpaRepository.findById(studyGroupId);
    }

    @Override
    public Optional<StudyGroup> findByIdForUpdate(Long studyGroupId) {
        System.out.println(">>>>>>>>>> findByIdForUpdate 호출");
        return groupJpaRepository.findByStudyGroupIdForUpdate(studyGroupId);
    }

    @Override
    public void delete(StudyGroup studyGroup) {
        groupJpaRepository.delete(studyGroup);
    }
}

