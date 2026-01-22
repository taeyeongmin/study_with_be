package com.ty.study_with_be.study_group.presentation.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "스터디 그룹 목록 응답")
@Getter
@AllArgsConstructor
public class StudyGroupListRes {

    @Schema(description = "목록 아이템")
    private List<StudyGroupListItem> items;

    @Schema(description = "현재 페이지(0부터)", example = "0")
    private int page;
    @Schema(description = "페이지 크기", example = "20")
    private int size;

    @Schema(description = "전체 요소 수", example = "153")
    private long totalElements;
    @Schema(description = "전체 페이지 수", example = "8")
    private int totalPages;

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private boolean hasNext;
}
