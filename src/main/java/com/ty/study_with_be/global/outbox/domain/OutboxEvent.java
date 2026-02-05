package com.ty.study_with_be.global.outbox.domain;

import com.ty.study_with_be.global.entity.BaseTimeEntity;
import com.ty.study_with_be.global.event.domain.EventStatus;
import com.ty.study_with_be.global.event.domain.EventType;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
    name = "outbox_event",
    indexes = {
        @Index(name = "idx_outbox_status_retry", columnList = "status, nextRetryAt"),
        @Index(name = "idx_outbox_created", columnList = "createdAt")
    }
)
public class OutboxEvent extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("이벤트 ID")
    private Long id;

    @Comment("이벤트 타입")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EventType eventType;

    @Comment("이벤트 본문")
    @Column(nullable = false, columnDefinition = "json")
    private String payload;

    @Comment("처리 상태")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EventStatus status;

    @Comment("재시도 횟수")
    @Column(nullable = false)
    private int retryCount;

    @Comment("다음 재시도 시각")
    private LocalDateTime nextRetryAt;

    @Comment("마지막 에러 메시지")
    @Column(length = 1000)
    private String lastError;

    public static OutboxEvent pending(EventType type, String payloadJson) {
        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.eventType = type;
        outboxEvent.payload = payloadJson;
        outboxEvent.status = EventStatus.PENDING;
        outboxEvent.retryCount = 0;
        outboxEvent.nextRetryAt = LocalDateTime.now(); // 즉시 처리 가능
        return outboxEvent;
    }

    public void markProcessing() {
        this.status = EventStatus.PROCESSING;
        this.lastError = null;
    }

    public void markDone() {
        this.status = EventStatus.DONE;
        this.lastError = null;
        this.nextRetryAt = null;
    }

    public void markFailed(Throwable ex, int maxRetry) {
        this.retryCount += 1;
        this.lastError = (ex == null ? "unknown" : String.valueOf(ex.getMessage()));

        if (this.retryCount >= maxRetry) {
            this.status = EventStatus.FAILED_FINAL; // 더 이상 재시도 안 함
            this.nextRetryAt = null;
            return;
        }

        this.status = EventStatus.FAILED;

        long delaySeconds = Math.min(60L, (long) Math.pow(2, Math.max(0, this.retryCount - 1)));
        this.nextRetryAt = LocalDateTime.now().plusSeconds(delaySeconds);
    }

    public void reviveFromStaleProcessing() {
        // 오래된 PROCESSING을 FAILED로 돌려 재시도 가능하게
        this.status = EventStatus.FAILED;
        this.nextRetryAt = LocalDateTime.now();
        this.lastError = "stale processing recovered";
    }
}
