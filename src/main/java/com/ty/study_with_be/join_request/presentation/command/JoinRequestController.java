package com.ty.study_with_be.join_request.presentation.command;

import com.ty.study_with_be.join_request.application.command.JoinRequestUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/study_group")
@RestController
@RequiredArgsConstructor
@Tag(name = "스터디 그룹 가입", description = "스터디 그룹 가입 API")
public class JoinRequestController {

    private final JoinRequestUseCase joinRequestUseCase;

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
    @PostMapping("/{studyGroupId}/join")
    public ResponseEntity<Void> joinRequest(
            @PathVariable Long studyGroupId,
            @AuthenticationPrincipal User user
    ){
        joinRequestUseCase.requestJoin(studyGroupId, Long.valueOf(user.getUsername()));

        return ResponseEntity.ok().build();
    }

}
