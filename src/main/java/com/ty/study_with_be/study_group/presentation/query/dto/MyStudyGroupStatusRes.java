package com.ty.study_with_be.study_group.presentation.query.dto;

import com.ty.study_with_be.study_group.domain.model.enums.MyStudyGroupStatus;
import com.ty.study_with_be.study_group.domain.model.enums.StudyRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MyStudyGroupStatusRes {

    private final MyStudyGroupStatus status;
    private final StudyRole studyRole;
    private final Long joinRequestId;

    public static MyStudyGroupStatusRes none() {
        return new MyStudyGroupStatusRes(
                MyStudyGroupStatus.NONE
                , StudyRole.NONE
                , null
        );
    }

    public static MyStudyGroupStatusRes pending(Long joinRequestId) {
        return new MyStudyGroupStatusRes(
                MyStudyGroupStatus.PENDING
                ,StudyRole.NONE
                , joinRequestId
        );
    }

    public static MyStudyGroupStatusRes joined(StudyRole role) {

        if (role == StudyRole.NONE)
            throw new RuntimeException("JOINED 상태에는 role이 필요합니다.");

        return new MyStudyGroupStatusRes(
                MyStudyGroupStatus.JOINED
                , role
                , null
        );
    }
}
