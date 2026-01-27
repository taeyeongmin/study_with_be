package com.ty.study_with_be.study_notice.presentation.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StudyNoticeItem {

    private Long noticeId;
    private String title;
    private String content;
    private Long writerId;
    private String nickname;
    private boolean pinned;
    private LocalDateTime createdAt;
}
