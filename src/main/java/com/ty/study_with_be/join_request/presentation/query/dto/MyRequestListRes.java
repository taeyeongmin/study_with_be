package com.ty.study_with_be.join_request.presentation.query.dto;

import com.ty.study_with_be.global.entity.PagingResEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MyRequestListRes extends PagingResEntity {

    @Schema(description = "목록 아이템")
    private List<MyRequestListItem> items;

    public MyRequestListRes(List<MyRequestListItem> items, int page, int size, long totalElements, int totalPages, boolean hasNext) {
        super(page, size, totalElements, totalPages, hasNext);
        this.items = items;
    }
}
