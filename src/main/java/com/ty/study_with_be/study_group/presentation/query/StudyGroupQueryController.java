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
    @GetMapping("/")
    @Operation(
            summary = "스터디 그룹 목록 조회",
            description = """
                    ## 기능 설명
                    - 필터 조건(카테고리/주제/지역/모드/모집상태)으로 목록을 조회.
                    - 페이징 파라미터(page, size)를 지원.
                    ---
                    ## 파라미터 설명
                    - title: 스터디명
                    - category: 카테고리 코드
                    - topic: 주제 검색어(부분 일치)
                    - region: 지역 코드
                    - studyMode: 스터디 모드(ONLINE/OFFLINE)
                    - recruitStatus: 모집 상태(RECRUITING/RECRUIT_END)
                    - page: 페이지 번호(0부터)
                    - size: 페이지 크기
                    """
    )
    public StudyGroupListRes getStudyGroups(
            @AuthenticationPrincipal User user
            , @ParameterObject @ModelAttribute StudyGroupListReq request
    ) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        long currentMemberId = 0L;
        if (user != null) currentMemberId = Long.parseLong(user.getUsername());

        return queryService.getStudyGroupList(
                request, pageable,currentMemberId
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
            @PathVariable Long studyGroupId,
            @AuthenticationPrincipal User user
    ){
        Long currentMemberId = null;
        if (user != null) currentMemberId = Long.valueOf(user.getUsername());

        StudyGroupDetailRes detail = queryService.getDetail(studyGroupId,currentMemberId);

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

    @PermitAll
    @GetMapping
    @Operation(
            summary = "인기 스터디 그룹 목록",
            description = """
                    ## 기능 설명
                    - 인기 있는 스터디 그룹 목록을 조회한다.
                    ---
                    ## 파라미터 설명
                    - 모집중 우선: recruitStatus === RECRUITING (필수)
                    - 운영중 우선: operationStatus === ONGOING (필수)
                    - 마감 임박 가중: dDay가 0~3일이면 가점
                    - 인기 가중: currentCount / capacity 비율이 높을수록 가점
                    """
    )
    public StudyGroupListRes getPopularStudy() {

        return queryService.getPopularStudy();
    }

}
