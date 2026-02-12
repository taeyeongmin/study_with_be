package com.ty.study_with_be.join_request.domain;

import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.join_request.application.query.JoinRequestQueryRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import com.ty.study_with_be.study_group.applicaiton.query.StudyGroupQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestJoinPolicy {

    private final StudyGroupQueryRepository studyGroupQueryRepository;
    private final JoinRequestQueryRepository joinRequestQueryRepository;

    public void validate(StudyGroup studyGroup, Long requesterId){

        studyGroup.validateRecruitable();

        if (studyGroupQueryRepository.existsMember(studyGroup.getStudyGroupId(), requesterId))
            throw new DomainException(ErrorCode.ALREADY_JOINED);

        if (joinRequestQueryRepository.existsPendingJoin(studyGroup.getStudyGroupId(), requesterId))
            throw new DomainException(ErrorCode.DUPLICATE_REQUEST);
    }

}
