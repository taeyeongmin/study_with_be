package com.ty.study_with_be.study_group.domain.model;

import com.ty.study_with_be.global.entity.BaseTimeEntity;
import com.ty.study_with_be.member.domain.model.Member;
import com.ty.study_with_be.study_group.domain.model.enums.JoinRequestStatus;
import com.ty.study_with_be.study_group.domain.model.enums.StudyStatus;
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
        @Index(name = "idx_join_req_member", columnList = "member_id")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinRequest extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long joinRequestId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "study_group_id", nullable = false)
    private StudyGroup studyGroup;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private JoinRequestStatus status;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "processed_reason", length = 300)
    private String processedReason;

    public static JoinRequest create(StudyGroup group, Member member) {
        // 요구사항: RECRUITING일 때만 신청 가능
        group.validateCanApply();

        JoinRequest r = new JoinRequest();
        r.studyGroup = group;
        r.member = member;
        r.status = JoinRequestStatus.PENDING;
        return r;
    }

    public void approve() {
        if (this.status != JoinRequestStatus.PENDING) throw new IllegalStateException("대기 상태만 승인할 수 있습니다.");
        // 승인 시점 검증: RECRUITING / RECRUIT_END 만 승인 가능
        if (!(studyGroup.getStatus() == StudyStatus.RECRUITING
           || studyGroup.getStatus() == StudyStatus.RECRUIT_END)) {
            throw new IllegalStateException("모집중/마감 상태가 아니면 승인할 수 없습니다.");
        }
        studyGroup.increaseMemberCount();
        this.status = JoinRequestStatus.APPROVED;
        this.processedAt = java.time.LocalDateTime.now();
    }

    public void reject(String reason) {
        if (this.status != JoinRequestStatus.PENDING) throw new IllegalStateException("대기 상태만 거부할 수 있습니다.");
        this.status = JoinRequestStatus.REJECTED;
        this.processedReason = reason;
        this.processedAt = java.time.LocalDateTime.now();
    }

    public void cancel() {
        if (this.status != JoinRequestStatus.PENDING) throw new IllegalStateException("대기 상태만 취소할 수 있습니다.");
        this.status = JoinRequestStatus.CANCELED;
        this.processedAt = java.time.LocalDateTime.now();
    }
}