package com.ty.study_with_be.join_request.infra;

import com.ty.study_with_be.join_request.domain.JoinRequestRepository;
import com.ty.study_with_be.study_group.domain.model.enums.JoinRequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JoinRequestRepositoryImpl implements JoinRequestRepository {

    private final JoinRequestJpaRepository jpaRepository;

    @Override
    public boolean existsPending(Long groupId, Long memberId) {

        return jpaRepository.existsByStudyGroupIdAndRequesterIdAndStatus(
                groupId,
                memberId,
                JoinRequestStatus.PENDING
        );
    }
}
