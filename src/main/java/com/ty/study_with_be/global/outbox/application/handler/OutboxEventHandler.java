package com.ty.study_with_be.global.outbox.application.handler;

import com.ty.study_with_be.global.event.domain.EventType;

import java.util.Set;

public interface OutboxEventHandler {

    EventType getType();

    default Set<EventType> getTypes() {
        return Set.of(getType());
    }

    void handle(EventType type, String payloadJson) throws Exception;
}
