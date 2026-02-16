package com.ty.study_with_be.study_group.presentation.query.dto;

import com.ty.study_with_be.global.entity.PagingResEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudyGroupListRes extends PagingResEntity {

    @Schema(description = "목록 아이템")
    private List<StudyGroupListItem> items;

    public StudyGroupListRes(List<StudyGroupListItem> items, int page, int size, long totalElements, int totalPages, boolean hasNext) {
        super(page, size, totalElements, totalPages, hasNext);
        this.items = items;
    }
}
