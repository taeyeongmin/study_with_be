package com.ty.study_with_be.global.outbox.infra.scheduler;

import com.ty.study_with_be.global.outbox.application.OutboxProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPollingScheduler {

    private final OutboxProcessor outboxProcessor;

    /**
     * 주기적으로 outbox 처리 트리거
     * - fixedDelay: 이전 실행이 끝난 후 delay만큼 대기
     */
    @Scheduled(fixedDelay = 7000)
    public void poll() {
        MDC.put("suppressHibernateSql", "true");
        try {
            outboxProcessor.drainOnce();
        } catch (Exception e) {
            log.error("[OUTBOX] polling drain failed", e);
        } finally {
            MDC.remove("suppressHibernateSql");
        }
    }
}
