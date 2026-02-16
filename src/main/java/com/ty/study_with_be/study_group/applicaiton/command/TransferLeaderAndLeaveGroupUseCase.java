package com.ty.study_with_be.study_group.applicaiton.command;

public interface TransferLeaderAndLeaveGroupUseCase {

    void leaveGroupLeader(Long groupId, Long currentMemberId, Long targetStudyMemberId);
}
