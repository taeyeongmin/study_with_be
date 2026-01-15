package com.ty.study_with_be.study_group.domain;

public interface GroupRepository {

    int countActiveByMemberId(Long memberId);

    boolean existActiveByMemberIdAndTitle(Long memberId, String title);
}
