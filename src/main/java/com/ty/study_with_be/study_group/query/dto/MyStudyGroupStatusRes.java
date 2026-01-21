package com.ty.study_with_be.study_group.query.dto;

import com.ty.study_with_be.study_group.domain.model.enums.MyStudyGroupStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MyStudyGroupStatusRes {

    private final MyStudyGroupStatus status;

    public static MyStudyGroupStatusRes none() {
        return new MyStudyGroupStatusRes(MyStudyGroupStatus.NONE);
    }

    public static MyStudyGroupStatusRes pending() {
        return new MyStudyGroupStatusRes(MyStudyGroupStatus.PENDING);
    }

    public static MyStudyGroupStatusRes joined() {
        return new MyStudyGroupStatusRes(MyStudyGroupStatus.JOINED);
    }
}