package com.ty.study_with_be.study_group.presentation.query;

import com.ty.study_with_be.study_group.applicaiton.query.StudyGroupQueryService;
import com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import com.ty.study_with_be.study_group.presentation.query.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/study_group")
@RestController
@RequiredArgsConstructor
@Tag(name = "스터디 그룹")
public class StudyGroupQueryController {

    private final StudyGroupQueryService queryService;

    @PermitAll
    @GetMapping
    @Operation(
            summary = "스터디 그룹 목록 조회",
            description = """
                    ## 기능 설명
                    - 필터 조건(카테고리/주제/지역/모드/모집상태)으로 목록을 조회.
                    - 페이징 파라미터(page, size)를 지원.
                    ---
                    ## 파라미터 설명
                    - category: 카테고리 코드
                    - topic: 주제 검색어(부분 일치)
                    - region: 지역 코드
                    - studyMode: 스터디 모드(ONLINE/OFFLINE)
                    - recruitStatus: 모집 상태(RECRUITING/RECRUIT_END)
                    - page: 페이지 번호(0부터)
                    - size: 페이지 크기
                    """
    )
    @Parameters({
            @Parameter(name = "category", description = "카테고리 코드", in = ParameterIn.QUERY),
            @Parameter(name = "topic", description = "주제 검색어(부분 일치)", in = ParameterIn.QUERY),
            @Parameter(name = "region", description = "지역 코드", in = ParameterIn.QUERY),
            @Parameter(name = "studyMode", description = "스터디 모드(ONLINE/OFFLINE)", in = ParameterIn.QUERY),
            @Parameter(name = "recruitStatus", description = "모집 상태(RECRUITING/RECRUIT_END)", in = ParameterIn.QUERY),
            @Parameter(name = "page", description = "페이지 번호(0부터)", in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "페이지 크기", in = ParameterIn.QUERY)
    })
    public StudyGroupListRes getStudyGroups(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) StudyMode studyMode,
            @RequestParam(required = false) RecruitStatus recruitStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return queryService.getStudyGroupList(
                category, topic, region, studyMode, recruitStatus, pageable
        );
    }

    @PermitAll
    @GetMapping("/{studyGroupId}")
    @Operation(
            summary = "스터디 그룹 상세 조회",
            description = """
                    ## 기능 설명
                    - 스터디 그룹의 상세 정보를 조회합니다.
                    """
    )
    @Parameter(name = "studyGroupId", description = "스터디 그룹 ID", in = ParameterIn.PATH)
    public StudyGroupDetailRes detailGroup(
            @PathVariable Long studyGroupId
    ){
        StudyGroupDetailRes detail = queryService.getDetail(studyGroupId);

        return detail;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{studyGroupId}/my_status")
    @Operation(
            summary = "내 스터디 그룹 상태 조회",
            description = """
                    ## 기능 설명
                    - 로그인 사용자의 스터디 그룹 상태를 조회합니다.
                    - NONE/PENDING/JOINED 중 하나를 반환합니다.
                    - 이미 가입된 상태(JOINED)이면 해당 그룹 내 ROLE 정보도 반환합니다.
                    """
    )
    @Parameter(name = "studyGroupId", description = "스터디 그룹 ID", in = ParameterIn.PATH)
    public MyStudyGroupStatusRes myStatus(
            @AuthenticationPrincipal User principal
            , @PathVariable Long studyGroupId
    ){

        MyStudyGroupStatusRes status = queryService.getMyStatus(studyGroupId, Long.valueOf(principal.getUsername()));

        return status;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{studyGroupId}/member/list")
    @Operation(
            summary = "스터디 그룹 회원 목록 조회",
            description = """
                    ## 기능 설명
                    - 해당 그룹의 회원 목록을 조회.
                    """
    )
    @Parameter(name = "studyGroupId", description = "스터디 그룹 ID", in = ParameterIn.PATH)
    public StudyMemberListRes getMemberList(
            @PathVariable Long studyGroupId
    ){

        return queryService.getStudyMemberList(studyGroupId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my")
    @Operation(
            summary = "내가 참여중인 그룹 목록 조회",
            description = """
                    ## 기능 설명
                    - 내가 참여중인 그룹의 목록을 조회
                    """
    )
    @Parameter(name = "studyGroupId", description = "스터디 그룹 ID", in = ParameterIn.PATH)
    public MyStudyGroupListRes getMyGroupList(
            @AuthenticationPrincipal User principal
            , @ParameterObject @ModelAttribute MyStudyGroupListReq request
    ){
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        return queryService.getMyGroupList(Long.valueOf(principal.getUsername()), request, pageable);
    }

}
