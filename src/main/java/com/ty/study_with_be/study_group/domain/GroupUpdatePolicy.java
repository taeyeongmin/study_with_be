package com.ty.study_with_be.study_group.domain;

import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupUpdatePolicy {

    private final GroupRepository groupRepository;

    public void valid(Long creatMemberId, String title, Long groupId) {

        if(groupRepository.existsActiveByMemberIdAndTitleExcludingGroupId(creatMemberId, title,groupId))
            throw new DomainException(ErrorCode.DUPLICATE_GROUP_TITLE);
    }

}
