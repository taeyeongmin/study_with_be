package com.ty.study_with_be.study_group.infra;

import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import com.ty.study_with_be.study_group.domain.model.enums.StudyStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor

public class GroupRepositoryImpl implements GroupRepository {

    private final GroupJpaRepository groupJpaRepository;

    private static final List<StudyStatus> ACTIVE_STATUSES =
            List.of(
                    StudyStatus.RECRUITING,
                    StudyStatus.ONGOING
            );

    @Override
    public int countActiveByMemberId(Long memberId) {

        return groupJpaRepository.countByOwnerIdAndStatusIn(memberId, ACTIVE_STATUSES);
    }

    @Override
    public boolean existActiveByMemberIdAndTitle(Long memberId,  String title) {

        return groupJpaRepository.existsByOwnerIdAndTitleAndStatusIn(memberId,title,ACTIVE_STATUSES);
    }

    @Override
    public void save(StudyGroup studyGroup) {
        groupJpaRepository.save(studyGroup);
    }

    @Override
    public Optional<StudyGroup> findById(Long studyGroupId) {
        return groupJpaRepository.findById(studyGroupId);
    }
}
