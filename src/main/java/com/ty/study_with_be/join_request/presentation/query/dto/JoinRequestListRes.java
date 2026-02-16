package com.ty.study_with_be.join_request.presentation.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class JoinRequestListRes {

    private Long studyGroupId;
    private int count;
    private List<JoinRequestListItem> items;

    public static JoinRequestListRes of(Long studyGroupId, List<JoinRequestListItem> items) {
        return new JoinRequestListRes(studyGroupId, items.size(), items);
    }
}