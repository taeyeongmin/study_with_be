package com.ty.study_with_be.join_request.application.query;

import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import com.ty.study_with_be.join_request.presentation.query.dto.JoinRequestListItem;
import com.ty.study_with_be.join_request.presentation.query.dto.MyRequestListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface JoinRequestQueryRepository {

    boolean existsPendingJoin(Long studyGroupId, Long memberId);

    Optional<Long> findPendingJoinRequestId(Long studyGroupId, Long memberId);

    List<JoinRequestListItem> findJoinRequests(Long studyGroupId, JoinRequestStatus status);

    int countByMemberIdPending(Long memberId);

    Page<MyRequestListItem> findMyRequests(Long memberId, JoinRequestStatus status, Pageable pageable);
}
