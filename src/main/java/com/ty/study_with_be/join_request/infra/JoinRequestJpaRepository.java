package com.ty.study_with_be.join_request.infra;

import com.ty.study_with_be.join_request.domain.model.JoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinRequestJpaRepository extends JpaRepository<JoinRequest, Long> {

}
