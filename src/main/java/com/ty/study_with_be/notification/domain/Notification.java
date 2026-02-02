package com.ty.study_with_be.notification.domain;

import com.ty.study_with_be.global.entity.BaseTimeEntity;
import com.ty.study_with_be.global.event.domain.EventType;
import jakarta.persistence.*;

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
    private Long id;

    /** 수신자 */
    @Column(nullable = false)
    private Long recipientMemberId;

    /** 알림 유형 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EventType type;

    /** 관련 스터디 그룹 */
    private Long studyGroupId;

    /** 행위자 (누가 이 이벤트를 발생시켰는지) */
    private Long actorMemberId;

    /** 대상자 (강퇴당한 사람, 역할 변경 대상 등) */
    private Long targetMemberId;

    /** FE 렌더링용 데이터 (title, groupName 등) */
    @Column(nullable = false, columnDefinition = "json")
    private String payload;

    /** 읽음 시각 */
    private LocalDateTime readAt;
}
