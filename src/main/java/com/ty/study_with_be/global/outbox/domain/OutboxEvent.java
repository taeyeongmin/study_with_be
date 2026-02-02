package com.ty.study_with_be.global.outbox.domain;

import com.ty.study_with_be.global.entity.BaseTimeEntity;
import com.ty.study_with_be.global.event.domain.EventStatus;
import com.ty.study_with_be.global.event.domain.EventType;
import jakarta.persistence.*;
import lombok.Getter;

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
    private Long id;

    /** 이벤트 종류 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EventType eventType;

    /** 이벤트 본문 */
    @Column(nullable = false, columnDefinition = "json")
    private String payload;

    /** 처리 상태 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EventStatus status;

    /** 재시도 횟수 */
    @Column(nullable = false)
    private int retryCount;

    /** 다음 재시도 시각 */
    private LocalDateTime nextRetryAt;

    /** 마지막 에러 메시지 */
    @Column(length = 1000)
    private String lastError;

    public static OutboxEvent pending(EventType type, String payloadJson) {
        OutboxEvent e = new OutboxEvent();
        e.eventType = type;
        e.payload = payloadJson;
        e.status = EventStatus.PENDING;
        e.retryCount = 0;
        e.nextRetryAt = LocalDateTime.now(); // 즉시 처리 가능
        return e;
    }

}
