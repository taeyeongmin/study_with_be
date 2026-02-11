package com.ty.study_with_be.join_request.application.query;

import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import com.ty.study_with_be.join_request.presentation.query.dto.JoinRequestListItem;
import com.ty.study_with_be.join_request.presentation.query.dto.JoinRequestListRes;
import com.ty.study_with_be.study_group.applicaiton.query.StudyGroupQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class JoinRequestQueryService {

    private final JoinRequestQueryRepository joinRequestQueryRepository;
    private final StudyGroupQueryRepository studyGroupQueryRepository;

    /** 현재 그룹에 대한 모든 신청 목록 조회 */
    public JoinRequestListRes getJoinRequests(Long studyGroupId, Long viewerMemberId, JoinRequestStatus status) {

        // 권한 체크
        if (!studyGroupQueryRepository.hasManagerRole(studyGroupId, viewerMemberId)) {
            throw new DomainException(ErrorCode.STUDY_GROUP_NOT_MANAGER);
        }

        JoinRequestStatus targetStatus = (status == null) ? JoinRequestStatus.PENDING : status;

        List<JoinRequestListItem> joinRequests = joinRequestQueryRepository.findJoinRequests(studyGroupId, targetStatus);

        return JoinRequestListRes.of(studyGroupId, joinRequests);
    }

    /** 현재 그룹에 대한 모든 신청 대기 목록 조회 */
    public int countByMemberIdPending(Long memberId) {
        return joinRequestQueryRepository.countByMemberIdPending(memberId);
    }

}
