package com.ty.study_with_be.notification.domain;

import com.ty.study_with_be.global.entity.BaseTimeEntity;
import com.ty.study_with_be.global.event.domain.EventType;
import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "notification",
    indexes = {
        @Index(name = "idx_notification_recipient", columnList = "recipientMemberId, createdAt"),
        @Index(name = "idx_notification_unread", columnList = "recipientMemberId, readAt")
    }
)
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("알림 PK")
    private Long id;

    @Comment("수신자")
    @Column(nullable = false)
    private Long recipientMemberId;

    @Comment("이벤트 유형")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EventType type;

    @Comment("관련 스터디 그룹")
    private Long studyGroupId;

    @Comment("행위자 ID")
    private Long actorMemberId;

    @Comment("대상자 ID")
    private Long targetMemberId;

    @Comment("알림 페이로드(JSON)")
    @Column(nullable = false, columnDefinition = "json")
    private String payload;

    @Comment("읽은 시간")
    private LocalDateTime readAt;

    public static Notification of(Long recipientMemberId,
                                  com.ty.study_with_be.global.event.domain.EventType type,
                                  Long studyGroupId,
                                  Long actorMemberId,
                                  Long targetMemberId,
                                  String payloadJson) {
        Notification notification = new Notification();
        notification.recipientMemberId = recipientMemberId;
        notification.type = type;
        notification.studyGroupId = studyGroupId;
        notification.actorMemberId = actorMemberId;
        notification.targetMemberId = targetMemberId;
        notification.payload = payloadJson;
        return notification;
    }
}
