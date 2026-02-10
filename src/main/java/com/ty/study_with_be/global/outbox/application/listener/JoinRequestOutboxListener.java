package com.ty.study_with_be.global.outbox.application.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.global.outbox.domain.OutboxEvent;
import com.ty.study_with_be.global.outbox.infra.repository.OutboxEventRepository;
import com.ty.study_with_be.join_request.domain.event.JoinCancelEvent;
import com.ty.study_with_be.join_request.domain.event.JoinProcessEvent;
import com.ty.study_with_be.join_request.domain.event.JoinRequestEvent;
import com.ty.study_with_be.global.outbox.application.dto.OutboxPayload;
import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 가입 신청과 관련된 이벤트를 수신하여 처리하는 객체
 * - 모두 도메인 로직과 하나의 트랜잭션으로 묶어 처리한다.
 */
@Component
@RequiredArgsConstructor
public class JoinRequestOutboxListener {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    /**
     * 스터디 그룹 신청에 대한 승인/거절 시
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onJoinProcess(JoinProcessEvent event) throws Exception {

        String payloadJson = objectMapper.writeValueAsString(
            OutboxPayload.of(
                event.getStudyGroupId(),
                event.getProcessorId(),
                event.getRequesterMemberId(),
                event.getRequesterMemberId()
            )
        );

        EventType eventType;

        if (event.getJoinRequestStatus() ==  JoinRequestStatus.APPROVED){
            eventType = EventType.JOIN_APPROVE;
        }else {
            eventType = EventType.JOIN_REJECT;
        }

        OutboxEvent outbox = OutboxEvent.pending(
                eventType,
                payloadJson
        );

        outboxEventRepository.save(outbox);
    }

    /**
     * 스터디 그룹 신청 취소 시
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onJoinRequestCancel(JoinCancelEvent event) throws Exception {

        String payloadJson = objectMapper.writeValueAsString(
            OutboxPayload.of(
                event.getStudyGroupId(),
                event.getRequesterMemberId(),
                null,
                event.getRequesterMemberId()
            )
        );

        OutboxEvent outbox = OutboxEvent.pending(
                EventType.JOIN_CANCEL,
                payloadJson
        );

        outboxEventRepository.save(outbox);
    }

    /**
     * 스터디 그룹 신청 시
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onJoinRequest(JoinRequestEvent event) throws Exception {

        String payloadJson = objectMapper.writeValueAsString(
            OutboxPayload.of(
                event.getStudyGroupId(),
                event.getRequesterMemberId(),
                null,
                event.getRequesterMemberId()
            )
        );

        OutboxEvent outbox = OutboxEvent.pending(
            EventType.JOIN_REQUEST,
            payloadJson
        );

        outboxEventRepository.save(outbox);
    }

}
