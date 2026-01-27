package com.ty.study_with_be.study_notice.presentation.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudyNoticeSaveReq {
    private Long noticeId;
    @NotNull
    private Long studyGroupId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private Boolean pinned;
}
