package com.ty.study_with_be.global.event.domain;

import java.time.LocalDateTime;

public abstract class DomainEvent {
    protected LocalDateTime createdAt;
    protected String eventId;
}