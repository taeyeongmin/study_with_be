package com.ty.study_with_be.join_request.presentation.query;

import com.ty.study_with_be.join_request.application.query.JoinRequestQueryService;
import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import com.ty.study_with_be.join_request.presentation.query.dto.JoinRequestListRes;
import com.ty.study_with_be.join_request.presentation.query.dto.MyRequestListReq;
import com.ty.study_with_be.join_request.presentation.query.dto.MyRequestListRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/study_group")
@RestController
@RequiredArgsConstructor
@Tag(name = "스터디 그룹 가입")
public class JoinRequestQueryController {

    private final JoinRequestQueryService joinRequestQueryService;

    @GetMapping("/{groupId}/join_requests")
    @Operation(
            summary = "스터디 그룹 가입 요청 목록 조회",
            description = """
                    ## 기능 설명
                    - 해당 스터디 그룹의 가입 요청 목록을 조회한다.
                    ---
                    ## 상세 설명
                    - 상태(status)로 필터 가능
                    - 해당 스터디 그룹의 리더/매니저만 조회 가능
                    """
    )
    @Parameters({
            @Parameter(name = "groupId", description = "스터디 그룹 ID", in = ParameterIn.PATH),
            @Parameter(name = "status", description = "가입 요청 상태 필터", in = ParameterIn.QUERY)
    })
    public JoinRequestListRes getJoinRequests(
            @PathVariable("groupId") Long groupId,
            @RequestParam(value = "status", required = false) JoinRequestStatus status,
            @AuthenticationPrincipal User user
    ) {
        Long viewerId = Long.valueOf(user.getUsername());
        return joinRequestQueryService.getJoinRequests(groupId, viewerId, status);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my/join_requests")
    @Operation(
            summary = "내 가입 요청 목록 조회",
            description = """
                    ## 기능 설명
                    - 내 가입 요청 목록 조회
                    - statusFilter + page/size로 미리보기/전체목록 모두 처리
                    """
    )
    public MyRequestListRes getMyGroupList(
            @AuthenticationPrincipal User principal,
            @ModelAttribute MyRequestListReq request
    ) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        return joinRequestQueryService.getMyRequestList(Long.valueOf(principal.getUsername()), request, pageable);
    }
}
