package com.ty.study_with_be.join_request.domain;

import com.ty.study_with_be.join_request.domain.model.JoinRequest;

import java.util.Optional;

public interface JoinRequestRepository {

    void save(JoinRequest joinRequest);

    Optional<JoinRequest> findById(Long requestId);
}
