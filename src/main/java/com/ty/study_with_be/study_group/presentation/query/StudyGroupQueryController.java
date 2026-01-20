package com.ty.study_with_be.study_group.presentation.query;

import com.ty.study_with_be.study_group.applicaiton.query.StudyGroupQueryService;
import com.ty.study_with_be.study_group.query.dto.MyStudyGroupStatusRes;
import com.ty.study_with_be.study_group.query.dto.StudyGroupDetailRes;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
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

    private final StudyGroupQueryService queryService;

    @PermitAll
    @GetMapping("/{studyGroupId}")
    public StudyGroupDetailRes detailGroup(
            @AuthenticationPrincipal User principal
            , @PathVariable Long studyGroupId
    ){
        StudyGroupDetailRes detail = queryService.getDetail(studyGroupId);

        return detail;
    }

    @PermitAll
    @GetMapping("/{studyGroupId}/my_status")
    public MyStudyGroupStatusRes myStatus(
            @AuthenticationPrincipal User principal
            , @PathVariable Long studyGroupId
    ){

        MyStudyGroupStatusRes status = queryService.getMyStatus(studyGroupId, Long.valueOf(principal.getUsername()));

        return status;
    }
}
