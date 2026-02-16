package com.ty.study_with_be.join_request.application.query;

import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import com.ty.study_with_be.join_request.presentation.query.dto.JoinRequestListItem;
import com.ty.study_with_be.join_request.presentation.query.dto.JoinRequestListRes;
import com.ty.study_with_be.join_request.presentation.query.dto.MyRequestListItem;
import com.ty.study_with_be.join_request.presentation.query.dto.MyRequestListReq;
import com.ty.study_with_be.join_request.presentation.query.dto.MyRequestListRes;
import com.ty.study_with_be.study_group.applicaiton.query.StudyGroupQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JoinRequestQueryService {

    private final JoinRequestQueryRepository joinRequestQueryRepository;
    private final StudyGroupQueryRepository studyGroupQueryRepository;

    public JoinRequestListRes getJoinRequests(Long studyGroupId, Long viewerMemberId, JoinRequestStatus status) {

        if (!studyGroupQueryRepository.hasManagerRole(studyGroupId, viewerMemberId)) {
            throw new DomainException(ErrorCode.STUDY_GROUP_NOT_MANAGER);
        }

        JoinRequestStatus targetStatus = (status == null) ? JoinRequestStatus.PENDING : status;
        List<JoinRequestListItem> joinRequests = joinRequestQueryRepository.findJoinRequests(studyGroupId, targetStatus);

        return JoinRequestListRes.of(studyGroupId, joinRequests);
    }

    public int countByMemberIdPending(Long memberId) {
        return joinRequestQueryRepository.countByMemberIdPending(memberId);
    }

    public MyRequestListRes getMyRequestList(
            Long memberId,
            MyRequestListReq request,
            Pageable pageable
    ) {
        JoinRequestStatus status = request.getStatusFilter().toStatusOrNull();
        Page<MyRequestListItem> page = joinRequestQueryRepository.findMyRequests(memberId, status, pageable);

        return new MyRequestListRes(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext()
        );
    }
}
