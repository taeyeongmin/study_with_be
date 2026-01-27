package com.ty.study_with_be.study_notice.presentation.command;

import com.ty.study_with_be.study_notice.application.command.StudyNoticeCreateUseCase;
import com.ty.study_with_be.study_notice.presentation.command.dto.StudyNoticeSaveReq;
import com.ty.study_with_be.study_notice.presentation.query.dto.StudyNoticeQueryReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study_group/{studyGroupId}/notice")
@Tag(name = "스터디 공지", description = "스터디 공지 관리 API")
public class StudyNoticeController {

    private final StudyNoticeCreateUseCase studyNoticeCreateUseCase;

    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "스터디 공지 생성",
            description = """
                    ## 기능 설명
                    - `스터디 그룹의 공지를 생성한다.`
                    ---
                    ## 상세 설명
                    - **그룹의 방장 혹은 매니저만 생성 가능하다.**
                    ---
                  
                    """
    )
    @PostMapping
    public ResponseEntity<Void> saveNotice(
            @Valid @RequestBody StudyNoticeSaveReq studyNoticeSaveReq
            , @AuthenticationPrincipal User user
    ) {
        studyNoticeCreateUseCase.createNotice(studyNoticeSaveReq, Long.valueOf(user.getUsername()));

        return ResponseEntity.ok().build();
    }
}
