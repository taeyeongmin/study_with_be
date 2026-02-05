package com.ty.study_with_be.notification.infra.sse;


import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SseEmitterRegistry {

    private final Map<Long, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter register(Long memberId, long timeoutMillis) {
        SseEmitter emitter = new SseEmitter(timeoutMillis);

        emitters.computeIfAbsent(memberId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> remove(memberId, emitter));
        emitter.onTimeout(() -> remove(memberId, emitter));
        emitter.onError(e -> remove(memberId, emitter));

        try {
            emitter.send(SseEmitter.event().name("connected").data("ok"));

        } catch (IOException e) {
            remove(memberId, emitter);
        }

        return emitter;
    }

    public List<SseEmitter> getEmitters(Long memberId) {
        return emitters.getOrDefault(memberId, new CopyOnWriteArrayList<>());
    }

    private void remove(Long memberId, SseEmitter emitter) {
        var list = emitters.get(memberId);
        if (list == null) return;
        list.remove(emitter);
        if (list.isEmpty()) emitters.remove(memberId);
    }
}