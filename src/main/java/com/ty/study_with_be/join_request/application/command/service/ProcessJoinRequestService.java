package com.ty.study_with_be.join_request.application.command.service;

import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.event.domain.DomainEvent;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.join_request.application.command.ProcessJoinRequestUseCase;
import com.ty.study_with_be.join_request.domain.JoinRequestRepository;
import com.ty.study_with_be.join_request.domain.model.JoinRequest;
import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessJoinRequestService implements ProcessJoinRequestUseCase {

    private final GroupRepository groupRepository;
    private final JoinRequestRepository joinRequestRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void process(Long studyGroupId, Long requestId, Long processorId, JoinRequestStatus status) {

        if (status != JoinRequestStatus.APPROVED && status != JoinRequestStatus.REJECTED)
            throw new DomainException(ErrorCode.INVALID_REQUEST_PRECESS_STATUS);

        // joinRequestEntity 조회
        JoinRequest joinRequest = joinRequestRepository.findById(requestId).orElseThrow(() -> new EntityNotFoundException("해당 요청이 존재하지 않습니다."));

        // URL 조작 방어
        if (!joinRequest.getStudyGroupId().equals(studyGroupId)) throw new RuntimeException("요청 확인 바람");

        // groupEntity 조회 (row-level lock)
        StudyGroup studyGroup = getStudyGroup(studyGroupId)
                .orElseThrow(() -> new EntityNotFoundException("해당 그룹이 존재하지 않습니다."));

        if (status == JoinRequestStatus.APPROVED) {
            studyGroup.joinMember(joinRequest.getRequesterId(), processorId);
            joinRequest.approve(processorId);
        } else {
            joinRequest.reject(processorId);
        }

        for (DomainEvent e : joinRequest.pullDomainEvents()) {
            eventPublisher.publishEvent(e);
        }
    }

    // stub 하기 위해 분리
    protected Optional<StudyGroup> getStudyGroup(Long studyGroupId) {
        return groupRepository.findByIdForUpdate(studyGroupId);
    }

}
