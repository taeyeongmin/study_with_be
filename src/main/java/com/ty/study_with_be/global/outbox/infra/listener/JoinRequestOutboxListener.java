package com.ty.study_with_be.global.outbox.infra.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.global.outbox.domain.OutboxEvent;
import com.ty.study_with_be.global.outbox.infra.repository.OutboxEventRepository;
import com.ty.study_with_be.join_request.domain.event.JoinCancelEvent;
import com.ty.study_with_be.join_request.domain.event.JoinProcessEvent;
import com.ty.study_with_be.join_request.domain.event.JoinRequestEvent;
import com.ty.study_with_be.global.outbox.application.dto.JoinProcessPayload;
import com.ty.study_with_be.global.outbox.application.dto.JoinRequestPayload;
import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

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
            JoinProcessPayload.of(
                event.getStudyGroupId(),
                event.getRequesterMemberId(),
                event.getProcessorId()
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
            JoinRequestPayload.of(
                event.getStudyGroupId(),
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
            JoinRequestPayload.of(
                event.getStudyGroupId(),
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
