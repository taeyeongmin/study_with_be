package com.ty.study_with_be.global.scheduler;

import com.ty.study_with_be.notification.infra.sse.SseEmitterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseHeartbeatScheduler {

    private final SseEmitterRegistry registry;

    /**
     * 하트비트 전송 메서드
     *
     * 역할:
     * - SSE 연결이 장시간 무음 상태면 중간 경로(CDN/프록시/네트워크/브라우저)가
     *   연결을 끊어버릴 수 있어 일정 시간주기로 브로드캐스팅 ping을 보내 안 끊겼다고 체크
     */
    @Scheduled(fixedRate = 50000)
    public void heartbeat() {

        log.info("[SSE][PING]");
        registry.sendToAll("ping", "1");
    }
}