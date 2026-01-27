package com.ty.study_with_be.study_notice.presentation.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StudyNoticeListRes {

    List<StudyNoticeItem> items;

}
