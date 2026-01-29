package com.ty.study_with_be.join_request.application.command.service;

import com.ty.study_with_be.join_request.application.command.JoinRequestCancelUseCase;
import com.ty.study_with_be.join_request.domain.JoinRequestRepository;
import com.ty.study_with_be.join_request.domain.model.JoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JoinRequestCancelService implements JoinRequestCancelUseCase {

    private final JoinRequestRepository joinRequestRepository;

    @Override
    @Transactional
    public void cancel(Long studyGroupId, Long joinRequestId, Long currentMemberId) {

        JoinRequest joinRequest = joinRequestRepository.findById(joinRequestId).orElseThrow(() -> new RuntimeException("가입 신청이 존재하지 않습니다."));

        // URL 조작 방어
        if (!joinRequest.getStudyGroupId().equals(studyGroupId)) throw new RuntimeException("요청 확인 바람");

        joinRequest.cancel(currentMemberId);

        joinRequestRepository.save(joinRequest);
    }
}
