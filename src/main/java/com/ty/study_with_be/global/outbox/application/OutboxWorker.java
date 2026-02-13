package com.ty.study_with_be.global.outbox.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ty.study_with_be.global.outbox.application.dto.OutboxPayload;
import com.ty.study_with_be.global.outbox.domain.OutboxEvent;
import com.ty.study_with_be.global.outbox.infra.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 배치를 돌며 outbox에 쌓인 event들의 상태를 변경하고
 * Notification 로직 처리 호출
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxWorker {

    private static final int BATCH_SIZE = 50;
    private static final int MAX_RETRY = 10;

    private final OutboxEventRepository outboxEventRepository;
    private final NotificationEventHandler notificationEventHandler;
    private final ObjectMapper objectMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int processBatch() {

        List<OutboxEvent> events = outboxEventRepository.lockBatchForProcessing(BATCH_SIZE);

        if (events.isEmpty()) return 0;

        for (OutboxEvent e : events) {
            try {
                e.markProcessing();
                // 같은 트랜잭션 안에서 Event 및 Notification 저장
                OutboxPayload outboxPayload = objectMapper.readValue(e.getPayload(), OutboxPayload.class);
                notificationEventHandler.precess(e.getEventType(), outboxPayload, outboxPayload.getRejectionReason());
                e.markDone();
            } catch (Exception ex) {
                log.error("[OUTBOX] fail id={}, type={}, retry={}", e.getId(), e.getEventType(), e.getRetryCount(), ex);
                e.markFailed(ex, MAX_RETRY);
            }
        }

        outboxEventRepository.saveAll(events);
        return events.size();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recoverStaleProcessing(int staleSeconds, int limit) {
        List<OutboxEvent> stale = outboxEventRepository.lockStaleProcessing(staleSeconds, limit);
        if (stale.isEmpty()) return;

        for (OutboxEvent e : stale) {
            e.reviveFromStaleProcessing();
        }
        outboxEventRepository.saveAll(stale);
    }
}
