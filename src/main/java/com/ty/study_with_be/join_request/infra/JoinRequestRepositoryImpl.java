package com.ty.study_with_be.join_request.infra;

import com.ty.study_with_be.join_request.domain.JoinRequestRepository;
import com.ty.study_with_be.join_request.domain.model.JoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JoinRequestRepositoryImpl implements JoinRequestRepository {

    private final JoinRequestJpaRepository jpaRepository;

    @Override
    public void save(JoinRequest joinRequest) {
        jpaRepository.save(joinRequest);
    }
}
