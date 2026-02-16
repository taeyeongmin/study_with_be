package com.ty.study_with_be.global.outbox.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxProcessor {

    private final OutboxWorker outboxWorker;

    /**
     * drain: 더 이상 처리할 것이 없을 때까지 batch 반복
     * - 스케줄러에서 호출
     * - outbox 이벤트 조회 후 이벤트 처리 명령
     */
    public void drainOnce() {

        // PROCESSING 상태 이벤트 복구
        outboxWorker.recoverStaleProcessing(60, 100);

        while (true) {
            int processed = outboxWorker.processBatch();
            if (processed == 0) return;
        }
    }
}
