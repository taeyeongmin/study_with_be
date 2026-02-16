package com.ty.study_with_be.join_request.infra;

import com.ty.study_with_be.join_request.domain.JoinRequestRepository;
import com.ty.study_with_be.join_request.domain.model.JoinRequest;
import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JoinRequestRepositoryImpl implements JoinRequestRepository {

    private final JoinRequestJpaRepository jpaRepository;

    @Override
    public JoinRequest save(JoinRequest joinRequest) {
        return jpaRepository.save(joinRequest);
    }

    @Override
    public Optional<JoinRequest> findById(Long requestId) {

        return jpaRepository.findById(requestId);
    }

    @Override
    public List<JoinRequest> findAllByStudyGroupIdAndPending(Long studyGroupId, JoinRequestStatus joinRequestStatus) {

        return jpaRepository.findAllByStudyGroupIdAndStatus(studyGroupId, joinRequestStatus);
    }

    @Override
    public void saveAll(List<JoinRequest> requests) {

        jpaRepository.saveAll(requests);
    }
}
