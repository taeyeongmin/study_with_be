package com.ty.study_with_be.join_request.application.command.service;

import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.join_request.application.command.ProcessJoinRequestUseCase;
import com.ty.study_with_be.join_request.domain.JoinRequestRepository;
import com.ty.study_with_be.join_request.domain.model.JoinRequest;
import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import com.ty.study_with_be.member.domain.repository.MemberRepository;
import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import com.ty.study_with_be.study_group.domain.model.StudyMember;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProcessJoinRequestService implements ProcessJoinRequestUseCase {

    private final GroupRepository groupRepository;
    private final JoinRequestRepository joinRequestRepository;

    @Override
    @Transactional
    public void process(Long studyGroupId, Long requestId, Long processorId, JoinRequestStatus status) {

        if (status != JoinRequestStatus.APPROVED && status != JoinRequestStatus.REJECTED)
            throw new DomainException(ErrorCode.INVALID_REQUEST_PRECESS_STATUS);

        // joinRequestEntity 조회
        JoinRequest joinRequest = joinRequestRepository.findById(requestId).orElseThrow(()->new EntityNotFoundException("해당 요청이 존재하지 않습니다."));

        // URL 조작 방어
        if (!joinRequest.getStudyGroupId().equals(studyGroupId)) throw new RuntimeException("요청 확인 바람");

        // groupEntity 조회
        StudyGroup studyGroup = groupRepository.findById(studyGroupId).orElseThrow(() -> new EntityNotFoundException("해당 그룹이 존재하지 않습니다."));
        StudyMember studyMember = studyGroup.findMember(processorId);

        if (!studyMember.hasPermission())
            throw new DomainException(ErrorCode.HAS_NOT_PERMISSION);

        if (status == JoinRequestStatus.APPROVED) {
            studyGroup.joinMember(joinRequest.getRequesterId());
            joinRequest.approve(processorId);
        } else {
            joinRequest.reject(processorId);
        }

        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
    }

}
