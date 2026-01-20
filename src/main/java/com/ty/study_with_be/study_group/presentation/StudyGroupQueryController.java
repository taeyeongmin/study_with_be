package com.ty.study_with_be.study_group.presentation;

import com.ty.study_with_be.study_group.applicaiton.StudyGroupQueryUseCase;
import com.ty.study_with_be.study_group.presentation.req.StudyGroupDetailRes;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/study_group")
@RestController
@RequiredArgsConstructor
@Tag(name = "스터디 그룹", description = "스터디 그룹 조회 API")
public class StudyGroupQueryController {

    private final StudyGroupQueryUseCase studyGroupQueryUseCase;

    @PermitAll
    @GetMapping("/{studyGroupId}")
    public ResponseEntity detailGroup(
            @AuthenticationPrincipal User principal
            , @PathVariable Long studyGroupId
    ){
        StudyGroupDetailRes detail = studyGroupQueryUseCase.getDetail(studyGroupId);

        return ResponseEntity.ok(detail);
    }
}
