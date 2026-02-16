package com.ty.study_with_be.join_request.infra;

import com.ty.study_with_be.join_request.domain.model.JoinRequest;
import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JoinRequestJpaRepository extends JpaRepository<JoinRequest, Long> {

    List<JoinRequest> findAllByStudyGroupIdAndStatus(Long studyGroupId, JoinRequestStatus status);
}
