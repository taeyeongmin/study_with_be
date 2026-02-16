package com.ty.study_with_be.notification.infra.sse;

import com.ty.study_with_be.notification.presentation.command.dto.SseNotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class SseEmitterRegistry {

    /**
     * memberId -> (connectionId -> EmitterHolder)
     *
     * - memberId: 사용자 식별자
     * - connectionId: 같은 사용자가 여러 탭/디바이스에서 접속할 수 있으므로 연결 단위를 구분하기 위한 UUID
     * - holder: emitter + 동시 send 방지 lock + 메타데이터를 묶은 객체
     */
    private final Map<Long, Map<String, EmitterHolder>> emitters = new ConcurrentHashMap<>();

    /**
     * 한 사용자당 허용할 SSE 연결 최대 개수.
     */
    private static final int MAX_CONNECTIONS_PER_MEMBER = 3;

    /**
     * SSE 연결을 등록하고, 연결 생명주기(완료/타임아웃/에러) 시 registry에서 제거되도록 hooks를 건 뒤 반환한다.
     *
     * @param memberId       SSE를 구독할 사용자 ID
     * @param timeoutMillis  SseEmitter timeout
     * @return 등록된 SseEmitter (컨트롤러에서 그대로 반환)
     */
    public SseEmitter register(Long memberId, long timeoutMillis) {
        String connectionId = UUID.randomUUID().toString();
        SseEmitter emitter = new SseEmitter(timeoutMillis);

        // 1) registry에 연결 추가 (연결 수 초과 시 오래된 연결 정리)
        emitters.compute(memberId, (id, map) -> {
            if (map == null) map = new ConcurrentHashMap<>();

            if (map.size() >= MAX_CONNECTIONS_PER_MEMBER) {
                removeOldest(map);
            }

            map.put(connectionId, new EmitterHolder(connectionId, emitter));
            return map;
        });

        // 2) 연결 종료/에러/타임아웃 시 registry에서 제거
        emitter.onCompletion(() -> remove(memberId, connectionId));
        emitter.onTimeout(() -> remove(memberId, connectionId));
        emitter.onError(e -> remove(memberId, connectionId));

        // 3) 최초 연결 확인 이벤트 (flush 역할)
        //    - 브라우저에서 "connected"를 수신하면 연결이 실제로 열렸음을 확인 가능
        //    - 이 send에서 실패하면 즉시 제거하여 유령 연결을 남기지 않음
        safeSend(memberId, connectionId, SseEmitter.event().name("connected").data("ok"));

        return emitter;
    }

    /**
     * 특정 사용자에게 SSE 이벤트를 전송.
     *
     * @param memberId  수신자
     * @param eventName SSE event name
     * @param data      전송 payload
     */
    public void sendToMember(Long memberId, String eventName, Object data) {
        Map<String, EmitterHolder> map = emitters.get(memberId);
        if (map == null || map.isEmpty()) return;

        // snapshot: 전송 중 remove가 일어나도 안전하도록 복사본 순회
        List<EmitterHolder> holders = new ArrayList<>(map.values());

        for (EmitterHolder holder : holders) {
            safeSend(memberId, holder.connectionId,
                    SseEmitter.event().name(eventName).data(data));
        }
    }

    /**
     * 모든 사용자에게 브로드캐스트 전송.
     * - 하트비트(ping)를 전체 커넥션에 보내는 용도
     *
     * @param eventName 이벤트명
     * @param data      payload
     */
    public void sendToAll(String eventName, Object data) {
        for (Long memberId : new ArrayList<>(emitters.keySet())) {
            sendToMember(memberId, eventName, data);
        }
    }

    /**
     * 안전한 send 래퍼
     *
     * 1) holder 존재 여부 확인 (이미 제거된 연결이면 skip)
     * 2) 동일 emitter에 대한 동시 send 방지 (lock)
     * 3) send 실패 시 즉시 registry에서 제거
     */
    private void safeSend(Long memberId, String connectionId, SseEmitter.SseEventBuilder event) {
        EmitterHolder holder = getHolder(memberId, connectionId);
        if (holder == null) return;

        holder.lock.lock();
        try {
            holder.emitter.send(event);
            holder.lastSentAt = Instant.now();
        } catch (IOException | IllegalStateException ex) {
            // send 실패 = 연결이 이미 죽었거나 응답 스트림이 닫혔다는 뜻
            remove(memberId, connectionId);
        } finally {
            holder.lock.unlock();
        }
    }

    /**
     * holder 조회
     *
     * @return 없으면 null
     */
    private EmitterHolder getHolder(Long memberId, String connectionId) {
        Map<String, EmitterHolder> map = emitters.get(memberId);
        if (map == null) return null;
        return map.get(connectionId);
    }

    /**
     * registry에서 특정 연결 제거
     *
     * - onCompletion/onTimeout/onError 콜백
     * - safeSend 실패 처리
     * - 연결 수 상한 초과 처리
     * 에서 공통으로 호출
     */
    private void remove(Long memberId, String connectionId) {
        Map<String, EmitterHolder> map = emitters.get(memberId);
        if (map == null) return;

        map.remove(connectionId);

        // 해당 사용자의 연결이 모두 제거되면 key 자체도 정리
        if (map.isEmpty()) {
            emitters.remove(memberId);
        }
    }

    /**
     * 연결 수 제한을 초과했을 때 가장 오래된 연결 1개를 제거한다.
     * (브라우저 자동 재연결 폭주 상황에서 registry가 무한히 커지는 것을 방지)
     */
    private void removeOldest(Map<String, EmitterHolder> map) {
        EmitterHolder oldest = null;
        for (EmitterHolder h : map.values()) {
            if (oldest == null || h.createdAt.isBefore(oldest.createdAt)) {
                oldest = h;
            }
        }

        if (oldest != null) {
            try {
                // complete()는 클라이언트가 빨리 끊김을 감지하고 재연결하도록 유도할 수 있음
                oldest.emitter.complete();
            } catch (Exception ignored) {
            }
            map.remove(oldest.connectionId);
        }
    }

    /**
     * emitter 1개(연결 1개)의 상태를 묶는 holder
     * - createdAt: 오래된 연결 정리 기준
     * - lastSentAt: 하트비트/전송 상태 추적용
     */
    private static class EmitterHolder {
        final String connectionId;
        final SseEmitter emitter;
        final ReentrantLock lock = new ReentrantLock();

        final Instant createdAt = Instant.now();
        volatile Instant lastSentAt = createdAt;

        EmitterHolder(String connectionId, SseEmitter emitter) {
            this.connectionId = connectionId;
            this.emitter = emitter;
        }
    }
}