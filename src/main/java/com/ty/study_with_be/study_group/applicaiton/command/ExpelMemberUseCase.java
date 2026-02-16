package com.ty.study_with_be.study_group.applicaiton.command;

public interface ExpelMemberUseCase {

    void expelMember(Long groupId,Long targetMemberId, Long loginMemberId);
}
