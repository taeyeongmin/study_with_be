package com.ty.study_with_be.study_group.presentation.query.dto;

import com.ty.study_with_be.study_group.domain.model.enums.StudyRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StudyMemberItem {

    private Long studyMemberId;
    private String nickname;
    private String email;
    private StudyRole  studyRole;
    private LocalDateTime joinDate;
}
