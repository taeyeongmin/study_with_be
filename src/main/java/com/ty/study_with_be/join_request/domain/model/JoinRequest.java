package com.ty.study_with_be.join_request.domain.model;

import com.ty.study_with_be.global.event.domain.DomainEvent;
import com.ty.study_with_be.global.entity.BaseTimeEntity;
import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.join_request.domain.event.JoinCancelEvent;
import com.ty.study_with_be.join_request.domain.event.JoinProcessEvent;
import com.ty.study_with_be.join_request.domain.event.JoinRequestEvent;
import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "join_request",
    indexes = {
        @Index(name = "idx_join_req_study_status", columnList = "study_group_id, status"),
        @Index(name = "idx_join_req_member", columnList = "requester_id")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinRequest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("가입 신청 ID")
    private Long joinRequestId;

    @Column(name = "study_group_id", nullable = false)
    @Comment("스터디 그룹 ID")
    private Long studyGroupId;

    @Column(name = "requester_id", nullable = false)
    @Comment("요청자 회원 PK")
    private Long requesterId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    @Comment("신청 상태")
    private JoinRequestStatus status;

    @Column(name = "processed_by_member_id")
    @Comment("처리자 회원 PK")
    private Long processedByMemberId;

    @Column(name = "processed_at")
    @Comment("처리 시간")
    private LocalDateTime processedAt;

    @Column(name = "processed_reason", length = 300)
    @Comment("처리 사유")
    private String processedReason;

    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public static JoinRequest create(Long studyGroupId, Long requesterId) {

        JoinRequest joinRequest = new JoinRequest();
        joinRequest.studyGroupId = studyGroupId;
        joinRequest.requesterId = requesterId;
        joinRequest.status = JoinRequestStatus.PENDING;

        joinRequest.raise(JoinRequestEvent.of(studyGroupId, requesterId));
        return joinRequest;
    }

    private void raise(DomainEvent event) {
        this.domainEvents.add(event);
    }

    /**
     * 이벤트를 꺼내면서 비운다(중복 발행 방지).
     */
    public List<DomainEvent> pullDomainEvents() {

        List<DomainEvent> events = new ArrayList<>(this.domainEvents);
        this.domainEvents.clear();

        return events;
    }

    public void approve(Long processorId) {

        validPending();
        this.status = JoinRequestStatus.APPROVED;
        this.processedByMemberId = processorId;
        this.processedAt = LocalDateTime.now();

        this.raise(JoinProcessEvent.of(studyGroupId, requesterId, processorId, JoinRequestStatus.APPROVED));
    }

    public void reject(Long processorId) {

        validPending();
        this.status = JoinRequestStatus.REJECTED;
        this.processedByMemberId = processorId;
        this.processedAt = LocalDateTime.now();

        this.raise(JoinProcessEvent.of(studyGroupId, requesterId, processorId, JoinRequestStatus.REJECTED));
    }

    private void validPending() {
        if (this.status != JoinRequestStatus.PENDING) throw new DomainException(ErrorCode.REQUEST_NOT_PENDING);
    }

    public void cancel(Long currentMemberId) {

        validCancel(currentMemberId);
        this.status = JoinRequestStatus.CANCELED;
        this.processedAt = LocalDateTime.now();

        this.raise(JoinCancelEvent.of(studyGroupId, currentMemberId));
    }

    private void validCancel(Long currentMemberId) {

        if (!this.requesterId.equals(currentMemberId)) throw new DomainException(ErrorCode.NOT_REQUEST_OWNER);

        if (this.status != JoinRequestStatus.PENDING) throw new DomainException(ErrorCode.REQUEST_NOT_PENDING);
    }
}
