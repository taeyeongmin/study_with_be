package com.ty.study_with_be.member.presentation.command;

import com.ty.study_with_be.member.application.command.UpdateProfileUseCase;
import com.ty.study_with_be.member.presentation.command.dto.UpdateProfileReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Tag(name = "회원", description = "회원 API")
public class MemberController {

    private final UpdateProfileUseCase updateProfileUseCase;

    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "회원 기본 정보 수정",
            description = """
                    ## 기능 설명
                    - 회원의 닉네임,email을 수정한다.
                    ---
                    ## 상세 설명
                    - **닉네임 중복 검증을 진행**.
                    
                    """
    )
    @PatchMapping("/profile")
    public ResponseEntity<Void> memberInfo(
            @AuthenticationPrincipal User principal
            , @RequestBody UpdateProfileReq req
    ){
        Long memberId = Long.parseLong(principal.getUsername());
        updateProfileUseCase.update(memberId, req);

        return ResponseEntity.ok().build();
    }

}
