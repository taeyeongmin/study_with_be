package com.ty.study_with_be.join_request.presentation.command;

import com.ty.study_with_be.join_request.application.command.JoinRequestUseCase;
import com.ty.study_with_be.join_request.application.command.ProcessJoinRequestUseCase;
import com.ty.study_with_be.join_request.presentation.query.dto.JoinRequestProcReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/study_group/{studyGroupId}")
@RestController
@RequiredArgsConstructor
@Tag(name = "스터디 그룹 가입", description = "스터디 그룹 가입 API")
public class JoinRequestController {

    private final JoinRequestUseCase joinRequestUseCase;
    private final ProcessJoinRequestUseCase processJoinRequestUseCase;

    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "스터디 가입 신청 처리",
            description = """
                    ## 기능 설명
                    - `스터디 가입 신청에 대한 승인/거절 처리를 한다.`
                    ---
                    ## 상세 설명
                    - **방장 및 매니저만 처리할 수 있다.**
                    - **정원이 이미 꽉 찬 경우 승인 불가**
                    - **승인 시점에 해당 그룹이 '모집'상태가 아니면 승인 불가**
                    - **PENDING 상태의 요청만 처리 가능**
                    
                    - **승인 시 바로 스터디 그룹의 현재원이 증가.**
                    ---
                    public enum ErrorCode {
                        STUDY_GROUP_NOT_OWNER,
                        JOIN_REQUEST_NOT_PENDING,
                        STUDY_GROUP_RECRUIT_CLOSED,
                        STUDY_GROUP_FULL
                    }
                    """
    )
    @PostMapping("/{requestId}/process")
    public ResponseEntity<Void> process(
            @PathVariable Long studyGroupId,
            @PathVariable Long requestId,
            @RequestBody @Valid JoinRequestProcReq req,
            @AuthenticationPrincipal User user
    ) {
        Long processorId = Long.valueOf(user.getUsername());

        processJoinRequestUseCase.process(studyGroupId, requestId, processorId, req.getStatus());

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "스터디 가입 신청",
            description = """
                    ## 기능 설명
                    - `스터디 가입을 요청한다.`
                    ---
                    ## 상세 설명
                    - **RECRUITING(모집) 상태의 스터디만 신청 가능하다**
                    - **이미 가입된 스터디에는 신청할 수 없다**
                    - **중복 신청할 수 없다.**
                    - **정원이 이미 꽉 찬 경우 신청 불가**
                    - **가입 신청 상태는 “대기”로 저장된다**
                    ---
                  
                    """
    )
    @PostMapping("/join")
    public ResponseEntity<Void> joinRequest(
            @PathVariable Long studyGroupId,
            @AuthenticationPrincipal User user
    ){
        joinRequestUseCase.requestJoin(studyGroupId, Long.valueOf(user.getUsername()));

        return ResponseEntity.ok().build();
    }
}
