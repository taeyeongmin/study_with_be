package com.ty.study_with_be.join_request.domain;

import com.ty.study_with_be.join_request.domain.model.JoinRequest;

public interface JoinRequestRepository {

    void save(JoinRequest joinRequest);
}
