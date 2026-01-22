package com.ty.study_with_be.join_request.application.query;

import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import com.ty.study_with_be.join_request.presentation.query.dto.JoinRequestListItem;

import java.util.List;

public interface JoinRequestQueryRepository {

    boolean existsPendingJoin(Long studyGroupId, Long memberId);

    List<JoinRequestListItem> findJoinRequests(Long studyGroupId, JoinRequestStatus status);
}
