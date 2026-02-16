package com.ty.study_with_be.member.application.query;

import com.ty.study_with_be.join_request.application.query.JoinRequestQueryService;
import com.ty.study_with_be.member.presentation.query.dto.DashBoardSummaryRes;
import com.ty.study_with_be.study_group.applicaiton.query.StudyGroupQueryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class MyPageSummaryService {

    private final StudyGroupQueryService studyGroupQueryService;
    private final JoinRequestQueryService joinRequestQueryService;

    public DashBoardSummaryRes getSummary(Long memberId) {

        return new DashBoardSummaryRes(
                studyGroupQueryService.countByMemberIdJoined(memberId)
                , studyGroupQueryService.countByMemberIdOperate(memberId)
                , joinRequestQueryService.countByMemberIdPending(memberId)
        );
    }
}
