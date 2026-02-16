package com.ty.study_with_be.join_request.application.command.service;

import com.ty.study_with_be.global.event.domain.DomainEvent;
import com.ty.study_with_be.join_request.application.command.JoinRequestUseCase;
import com.ty.study_with_be.join_request.domain.JoinRequestRepository;
import com.ty.study_with_be.join_request.domain.RequestJoinPolicy;
import com.ty.study_with_be.join_request.domain.model.JoinRequest;
import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JoinRequestService implements JoinRequestUseCase {

    private final GroupRepository groupRepository;
    private final JoinRequestRepository joinRequestRepository;
    private final RequestJoinPolicy requestJoinPolicy;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void requestJoin(Long studyGroupId, Long memberId) {
        
        // 그룹 entity 조회
        StudyGroup studyGroup = groupRepository.findById(studyGroupId).orElseThrow(() -> new RuntimeException("해당 그룹이 없습니다."));
        
        // 가입 신청 검증
        requestJoinPolicy.validate(studyGroup, memberId);

        // entity 생성
        JoinRequest joinRequest = JoinRequest.create(studyGroupId, memberId);

        // DB 저장
        joinRequestRepository.save(joinRequest);

        for (DomainEvent e : joinRequest.pullDomainEvents()) {
            eventPublisher.publishEvent(e);
        }
    }
}
