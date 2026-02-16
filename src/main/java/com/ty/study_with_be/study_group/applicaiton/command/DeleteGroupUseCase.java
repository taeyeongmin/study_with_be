package com.ty.study_with_be.study_group.applicaiton.command;

public interface DeleteGroupUseCase {

    void deleteGroup(Long groupId, Long memberId);
}
