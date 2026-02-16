package com.ty.study_with_be.study_group.presentation.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StudyMemberListRes {

    List<StudyMemberItem> members;
}
