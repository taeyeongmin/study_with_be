package com.ty.study_with_be.join_request.application.command.service;

import com.ty.study_with_be.join_request.application.command.RejectAllJoinRequestUseCase;
import com.ty.study_with_be.join_request.domain.JoinRequestRepository;
import com.ty.study_with_be.join_request.domain.model.JoinRequest;
import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RejectAllJoinRequestService implements RejectAllJoinRequestUseCase {

    private final JoinRequestRepository joinRequestRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void rejectAll(Long studyGroupId, Long processorMemberId) {

        List<JoinRequest> requests =  joinRequestRepository.findAllByStudyGroupIdAndPending(studyGroupId, JoinRequestStatus.PENDING);

        requests.forEach(joinRequest-> joinRequest.rejectByEndOperation(processorMemberId));

        joinRequestRepository.saveAll(requests);

        requests.stream()
                .flatMap(request -> request.pullDomainEvents().stream())
                .forEach(eventPublisher::publishEvent);
    }
}
