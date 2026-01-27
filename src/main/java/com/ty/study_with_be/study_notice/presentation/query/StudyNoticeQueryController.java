package com.ty.study_with_be.study_notice.presentation.query;

import com.ty.study_with_be.study_notice.application.query.StudyNoticeQueryService;
import com.ty.study_with_be.study_notice.presentation.query.dto.StudyNoticeListRes;
import com.ty.study_with_be.study_notice.presentation.query.dto.StudyNoticeQueryReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "스터디 그룹 공지")
@RequestMapping("/api/study_group/{studyGroupId}/notice")
public class StudyNoticeQueryController {

    private final StudyNoticeQueryService studyNoticeQueryService;

    @Operation(
            summary = "스터디 그룹 공지 목록 조회",
            description = """
                    ## 기능 설명
                    - 필터 조건(카테고리/주제/지역/모드/모집상태)으로 목록을 조회.
                    - 페이징 지원 X.
                    ---
                    ## 상세 설명
                    - 해당 그룹의 회원만 조회 가능
                    ---
                    """
    )
    @GetMapping("/list")
    public StudyNoticeListRes getNoticeList(
        @PathVariable Long studyGroupId,
        StudyNoticeQueryReq param,
        @AuthenticationPrincipal User user
    ){
        return studyNoticeQueryService.getNotices(studyGroupId,Long.valueOf(user.getUsername()),param);
    }
}
