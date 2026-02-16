package com.ty.study_with_be.member.presentation.query;

import com.ty.study_with_be.member.application.query.MyPageSummaryService;
import com.ty.study_with_be.member.presentation.query.dto.DashBoardSummaryRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 마이 페이지 대시보드의 count 집계에 대한 API 제공
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/my_page")
@Tag(name = "마이페이지", description = "마이페이지 대시보드 API")
public class MyPageDashBoardQueryController {

    private final MyPageSummaryService myPageSummaryService;

    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "마이페이지 대시보드 집계",
            description = """
                    ## 기능 설명
                    - 나의 참여 그룹(방장X),운영 중인 그룹(방장), 가입 신청 갯수를 제공
                    """
    )
    @GetMapping("/summary")
    public ResponseEntity<DashBoardSummaryRes> myPageSummary(@AuthenticationPrincipal User principal){

        Long memberId = Long.parseLong(principal.getUsername());
        DashBoardSummaryRes summary = myPageSummaryService.getSummary(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(summary);
    }
}
