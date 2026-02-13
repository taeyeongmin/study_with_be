package com.ty.study_with_be.study_group.presentation.command;

import com.ty.study_with_be.study_group.applicaiton.command.*;
import com.ty.study_with_be.study_group.presentation.command.dto.StudyGroupOperationInfoUpdateReq;
import com.ty.study_with_be.study_group.presentation.command.dto.StudyGroupReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/study_group")
@RestController
@RequiredArgsConstructor
@Tag(name = "스터디 그룹", description = "스터디 그룹 생성/관리 API")
public class StudyGroupMngController {

    private final CreateGroupUseCase createGroupUseCase;
    private final UpdateGroupUseCase updateGroupUseCase;
    private final DeleteGroupUseCase deleteGroupUseCase;
    private final RecruitEndUseCase recruitEndUseCase;
    private final RecruitResumeUseCase recruitResumeUseCase;
    private final OperationEndUseCase operationEndUseCase;

    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "스터디 그룹 생성",
            description = """
                    ## 기능 설명
                    - `스터디 그룹을 생성한다.`
                    ---
                    ## 상세 설명
                    - **한 회원은 모집중 또는 진행중 상태의 스터디 그룹을 최대 N개까지만 생성할 수 있다.**
                    - **스터디 정원은 2명 이상 N명 이하만 허용된다.**
                    - **오프라인 스터디의 경우 지역 정보는 필수이다.**
                    - **한 회원은 모집중 또는 진행중 상태의 스터디 그룹을 동일한 이름으로 생성할 수 없다.**
                    ---
                  
                    """
    )
    @PostMapping
    public ResponseEntity createGroup(@Valid @RequestBody StudyGroupReq studyGroupReq, @AuthenticationPrincipal User principal){

        createGroupUseCase.create(studyGroupReq, Long.valueOf(principal.getUsername()));

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{studyGroupId}")
    @Operation(
            summary = "스터디그룹 기본정보 수정",
            description = """
                    ## 기능 설명
                    - `스터디그룹의 기본정보를 수정한다.`
                    ---
                    ## 상세 설명
                    - **리더만 기본정보를 수정할 수 있다.**
                    - **종료된 스터디는 수정할 수 없다.**
                    - **오프라인 스터디는 지역 정보가 필수다.**
                    """
    )
    public ResponseEntity updateGroup(
            @Valid @RequestBody StudyGroupReq studyGroupReq
            , @AuthenticationPrincipal User principal
            , @PathVariable Long studyGroupId
    ){

        updateGroupUseCase.updateAll(studyGroupReq, Long.valueOf(principal.getUsername()),studyGroupId);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{studyGroupId}/operation_info")
    @Operation(
            summary = "스터디그룹 운영정보 수정",
            description = """
                    ## 기능 설명
                    - `스터디그룹 운영정보(정원, 모드, 일정)를 수정한다.`
                    ---
                    ## 상세 설명
                    - **오프라인 스터디는 지역 정보가 필수다.**
                    - **정원은 최소 2명 이상이어야 한다.**
                    """
    )
    public ResponseEntity updateOperationInfo(
            @PathVariable Long studyGroupId,
            @AuthenticationPrincipal User principal,
            @Valid @RequestBody StudyGroupOperationInfoUpdateReq req
    ) {
        updateGroupUseCase.updateOperationInfo(studyGroupId, req, Long.valueOf(principal.getUsername()));

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{studyGroupId}")
    @Operation(
            summary = "스터디그룹 삭제",
            description = """
                    ## 기능 설명
                    - `스터디그룹을 제거한다.`
                    ---
                    ## 상세 설명
                    - **방장만 삭제 가능하다.**
                    - **방장 혼자만 참여 중일 때만 삭제 가능하다.**
                    - **삭제 시 물리 삭제를 수행한다.**
                    """
    )
    public ResponseEntity deleteGroup(
            @PathVariable Long studyGroupId
            , @AuthenticationPrincipal User principal
    ) {
        deleteGroupUseCase.deleteGroup(studyGroupId, Long.valueOf(principal.getUsername()));

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{studyGroupId}/recruit_end")
    @Operation(
            summary = "스터디그룹 모집 종료",
            description = """
                    ## 기능 설명
                    - `모집 상태를 종료로 전환한다.`
                    ---
                    ## 상세 설명
                    - **모든 가입 신청에 대해 거절 처리를 수행.**
                    - **방장만 처리 가능하다.**
                    - **모집중인 상태만 가능하다..**
                    """
    )
    public ResponseEntity<Void> recruitEnd(
            @PathVariable Long studyGroupId
            , @AuthenticationPrincipal User principal
    ) {
        recruitEndUseCase.endRecruitment(studyGroupId, Long.valueOf(principal.getUsername()));

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{studyGroupId}/recruit_resume")
    @Operation(
            summary = "스터디그룹 모집 재개",
            description = """
                    ## 기능 설명
                    - `모집 상태를 모집으로 전환한다.`
                    ---
                    ## 상세 설명
                    - **방장만 처리 가능하다.**
                    - **모집종료인 상태만 가능하다.**
                    - **모집 마감 기간이 지나지 않아야한다.**
                    - **모집 정원이 초과되지 않아야한다.**
                    """
    )
    public ResponseEntity<Void> recruitResume(
            @PathVariable Long studyGroupId
            , @AuthenticationPrincipal User principal
    ) {
        recruitResumeUseCase.resumeRecruitment(studyGroupId, Long.valueOf(principal.getUsername()));

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{studyGroupId}/operation_end")
    @Operation(
            summary = "스터디그룹 운영 종료",
            description = """
                    ## 기능 설명
                    - `운영 상태를 종료로 전환한다.`
                    ---
                    ## 상세 설명
                    - **방장만 처리 가능하다.**
                    - **모집 종료 상태만 가능하다.**
                    - **종료한 스터디는 다시 재개할 수 없다.**
                    """
    )
    public ResponseEntity<Void> operationEnd(
            @PathVariable Long studyGroupId
            , @AuthenticationPrincipal User principal
    ) {
        operationEndUseCase.endOperation(studyGroupId, Long.valueOf(principal.getUsername()));

        return ResponseEntity.ok().build();
    }
}
