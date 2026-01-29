package com.ty.study_with_be.study_group.presentation.command;

import com.ty.study_with_be.study_group.applicaiton.command.ChangeStudyMemberRoleUseCase;
import com.ty.study_with_be.study_group.applicaiton.command.ExpelMemberUseCase;
import com.ty.study_with_be.study_group.applicaiton.command.LeaveGroupUseCase;
import com.ty.study_with_be.study_group.applicaiton.command.TransferLeaderAndLeaveGroupUseCase;
import com.ty.study_with_be.study_group.presentation.command.dto.LeaderLeaveReq;
import com.ty.study_with_be.study_group.presentation.command.dto.StudyMemberRoleChangeReq;
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
@Tag(name = "스터디 그룹 멤버 관리", description = "스터디 그룹 멤버 관리 API")
public class StudyGroupMemberController {

    private final LeaveGroupUseCase leaveGroupUseCase;
    private final ExpelMemberUseCase expelMemberUseCase;
    private final ChangeStudyMemberRoleUseCase changeStudyMemberRoleUseCase;
    private final TransferLeaderAndLeaveGroupUseCase transferLeaderAndLeaveGroupUseCase;


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{studyGroupId}/leader_leave")
    @Operation(
            summary = "스터디그룹 탈퇴(방장)",
            description = """
                    ## 기능 설명
                    - `스터디그룹을 탈퇴한다(방장 전용).`
                    ---
                    ## 상세 설명
                    - **방장이 다른 인원에게 방장위임 후 탈퇴한다.
                    - **종료 된 그룹에 대해서는 탈퇴 불가.**
                    """
    )
    public ResponseEntity leaveGroupLeader(
            @PathVariable Long studyGroupId
            , @AuthenticationPrincipal User principal
            , @Valid @RequestBody LeaderLeaveReq req
    ) {
        transferLeaderAndLeaveGroupUseCase.leaveGroupLeader(studyGroupId, Long.valueOf(principal.getUsername()),req.getStudyMemberId());

        return ResponseEntity.ok().build();
    }


    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{studyGroupId}/leave")
    @Operation(
            summary = "스터디그룹 탈퇴",
            description = """
                    ## 기능 설명
                    - `스터디그룹을 탈퇴한다.`
                    ---
                    ## 상세 설명
                    - **방장은 탈퇴 불가.
                    - **종료 된 그룹에 대해서는 탈퇴 불가.**
                    """
    )
    public ResponseEntity leaveGroup(
            @PathVariable Long studyGroupId
            , @AuthenticationPrincipal User principal
    ) {
        leaveGroupUseCase.leaveGroup(studyGroupId, Long.valueOf(principal.getUsername()));

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{studyGroupId}/remove/{memberId}")
    @Operation(
            summary = "스터디그룹 강제 탈퇴",
            description = """
                    ## 기능 설명
                    - `스터디그룹을 탈퇴한다.`
                    ---
                    ## 상세 설명
                    - **본인 캉퇴 불가
                    - **방장은 누구든 강퇴 가능.
                    - **리더는 MEMBER만 강퇴 가능.
                    - **종료 된 그룹에 대해서는 탈퇴 불가.**
                    """
    )
    public ResponseEntity expelMember(
            @PathVariable Long studyGroupId
            , @PathVariable Long memberId
            , @AuthenticationPrincipal User principal
    ) {
        expelMemberUseCase.expelMember(studyGroupId, memberId, Long.valueOf(principal.getUsername()));

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "스터디그룹 멤버 역할 변경",
            description = """
                    ## 기능 설명
                    - `스터디그룹 멤버의 역할을 변경한다.`
                    ---
                    ## 상세 설명
                    - **방장만 할 수 있다.
                    - **해당 그룹에 속한 멤버에 대해 변경.
                    - **종료 상태인 그룹에 대해선 불가능.
                    """
    )
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{studyGroupId}/members/{memberId}/role")
    public ResponseEntity<Void> changeMemberRole(
            @PathVariable Long studyGroupId
            , @PathVariable Long memberId
            , @AuthenticationPrincipal User principal
            , @RequestBody StudyMemberRoleChangeReq req
    ){
        changeStudyMemberRoleUseCase.change(studyGroupId,memberId,Long.valueOf(principal.getUsername()),req.getRole());

        return ResponseEntity.ok().build();
    }
}
