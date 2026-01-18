package com.ty.study_with_be.study_group.presentation;

import com.ty.study_with_be.study_group.applicaiton.CreateGroupUseCase;
import com.ty.study_with_be.study_group.applicaiton.StudyGroupQueryUseCase;
import com.ty.study_with_be.study_group.applicaiton.UpdateGroupUseCase;
import com.ty.study_with_be.study_group.presentation.req.StudyGroupDetailRes;
import com.ty.study_with_be.study_group.presentation.req.StudyGroupReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/study_group")
@RestController
@RequiredArgsConstructor
@Tag(name = "스터디 그룹", description = "스터디 그룹 조회 API")
public class StudyGroupQueryController {

    private final StudyGroupQueryUseCase studyGroupQueryUseCase;

    @GetMapping("/{studyGroupId}")
    public ResponseEntity detailGroup(
            @AuthenticationPrincipal User principal
            , @PathVariable Long studyGroupId
    ){
        StudyGroupDetailRes detail = studyGroupQueryUseCase.getDetail(studyGroupId);

        return ResponseEntity.ok(detail);
    }
}
