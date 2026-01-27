package com.ty.study_with_be.join_request.domain.model;

import com.ty.study_with_be.global.entity.BaseTimeEntity;
import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
public class
JoinRequest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long joinRequestId;

    @Column(name = "study_group_id", nullable = false)
    private Long studyGroupId;

    @Column(name = "requester_id", nullable = false)
    private Long requesterId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private JoinRequestStatus status;

    @Column(name = "processed_by_member_id")
    private Long processedByMemberId;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "processed_reason", length = 300)
    private String processedReason;

    public static JoinRequest create(Long studyGroupId, Long requesterId) {

        JoinRequest joinRequest = new JoinRequest();
        joinRequest.studyGroupId = studyGroupId;
        joinRequest.requesterId = requesterId;
        joinRequest.status = JoinRequestStatus.PENDING;
        return joinRequest;
    }

    public void approve(Long processorId) {
        validPending();
        this.status = JoinRequestStatus.APPROVED;
        this.processedByMemberId = processorId;
        this.processedAt = LocalDateTime.now();
    }

    public void reject(Long processorId) {
        validPending();
        this.status = JoinRequestStatus.REJECTED;
        this.processedByMemberId = processorId;
        this.processedAt = LocalDateTime.now();
    }

    private void validPending(){
        if (this.status != JoinRequestStatus.PENDING) throw new DomainException(ErrorCode.REQUEST_NOT_PENDING);
    }

    public void cancel() {
        if (this.status != JoinRequestStatus.PENDING) throw new IllegalStateException("대기 상태만 취소할 수 있습니다.");
        this.status = JoinRequestStatus.CANCELED;
        this.processedAt = LocalDateTime.now();
    }

}