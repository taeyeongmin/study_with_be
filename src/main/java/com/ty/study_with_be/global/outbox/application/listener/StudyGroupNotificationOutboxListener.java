package com.ty.study_with_be.global.outbox.application.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.global.outbox.application.dto.OutboxPayload;
import com.ty.study_with_be.global.outbox.domain.OutboxEvent;
import com.ty.study_with_be.global.outbox.infra.repository.OutboxEventRepository;
import com.ty.study_with_be.study_notice.domain.event.CreateGroupNoticeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 스터디 그룹 공지와 관련된 이벤트를 수신하여 처리하는 객체
 */
@Component
@RequiredArgsConstructor
public class StudyGroupNotificationOutboxListener {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    /**
     * 스터디 그룹 공지 등록 시
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onCreateGroupNotice(CreateGroupNoticeEvent event) throws Exception {

        String payloadJson = objectMapper.writeValueAsString(
                OutboxPayload.of(
                        event.getStudyGroupId(),
                        event.getProcessorMemberId(),
                        null,
                        null
                )
        );

        OutboxEvent outbox = OutboxEvent.pending(
                EventType.NOTICE_CREATE,
                payloadJson
        );

        outboxEventRepository.save(outbox);
    }
    
}
