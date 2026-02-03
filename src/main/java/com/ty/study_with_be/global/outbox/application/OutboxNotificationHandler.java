package com.ty.study_with_be.global.outbox.application;

import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.global.outbox.application.handler.OutboxEventHandler;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class OutboxNotificationHandler {

    private final Map<EventType, OutboxEventHandler> handlerMap;

    public OutboxNotificationHandler(List<OutboxEventHandler> handlers) {

        Map<EventType, OutboxEventHandler> map = new EnumMap<>(EventType.class);

        for (OutboxEventHandler handler : handlers) {
            for (EventType type : handler.getTypes()) {
                map.put(type, handler);
            }
        }
        this.handlerMap = map;
    }

    public void handle(EventType type, String payloadJson) throws Exception {

        OutboxEventHandler handler = handlerMap.get(type);

        if (handler == null) {
            throw new RuntimeException("지원하지 않는 유형입니다: " + type);
        }
        handler.handle(type, payloadJson);
    }
}
