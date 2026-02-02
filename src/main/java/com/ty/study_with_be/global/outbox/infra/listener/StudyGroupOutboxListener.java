package com.ty.study_with_be.global.outbox.infra.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.global.outbox.domain.OutboxEvent;
import com.ty.study_with_be.global.outbox.infra.repository.OutboxEventRepository;
import com.ty.study_with_be.study_group.domain.event.ChangeRoleEvent;
import com.ty.study_with_be.study_group.domain.event.MemberKickEvent;
import com.ty.study_with_be.study_group.domain.event.MemberLeaveEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class StudyGroupOutboxListener {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;


    /**
     * 스터디 그룹 멤버 role 변경
     */

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onChangeRole(ChangeRoleEvent event) throws Exception {

        String payloadJson = objectMapper.writeValueAsString(
                Map.of(
                        "studyGroupId", event.getStudyGroupId(),
                        "leaveMemberId", event.getTargetMemberId(),
                        "processorId", event.getProcessorMemberId()
                )
        );

        int a = 2;

        OutboxEvent outbox = OutboxEvent.pending(
                EventType.ROLE_CHANGE,
                payloadJson
        );

        outboxEventRepository.save(outbox);
    }
    
    /**
     * 스터디 그룹 강퇴 시
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onKickMember(MemberKickEvent event) throws Exception {

        String payloadJson = objectMapper.writeValueAsString(
                Map.of(
                        "studyGroupId", event.getStudyGroupId(),
                        "leaveMemberId", event.getLeaveMemberId(),
                        "processorId", event.getProcessorMemberId()
                )
        );

        OutboxEvent outbox = OutboxEvent.pending(
                EventType.MEMBER_KICK,
                payloadJson
        );

        outboxEventRepository.save(outbox);
    }

    /**
     * 스터디 그룹 탈퇴 시
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onLeaveMember(MemberLeaveEvent event) throws Exception {

        String payloadJson = objectMapper.writeValueAsString(
                Map.of(
                        "studyGroupId", event.getStudyGroupId(),
                        "leaveMemberId", event.getLeaveMemberId()
                )
        );

        OutboxEvent outbox = OutboxEvent.pending(
                EventType.MEMBER_LEAVE,
                payloadJson
        );

        outboxEventRepository.save(outbox);
    }


}