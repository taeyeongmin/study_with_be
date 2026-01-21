package com.ty.study_with_be.study_group.domain.model;

import com.ty.study_with_be.global.entity.BaseTimeEntity;
import com.ty.study_with_be.member.domain.model.Member;
import com.ty.study_with_be.study_group.domain.model.enums.OperationStatus;
import com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.management.OperatingSystemMXBean;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "notice",
    indexes = {
        @Index(name = "idx_notice_study", columnList = "study_group_id"),
        @Index(name = "idx_notice_pinned", columnList = "study_group_id, pinned, pinned_at"),
        @Index(name = "idx_notice_created_at", columnList = "study_group_id, created_at")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "study_group_id", nullable = false)
    private StudyGroup studyGroup;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "writer_member_id", nullable = false)
    private Member writer;

    @Column(name = "title", nullable = false, length = 80)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "pinned", nullable = false)
    private boolean pinned;

    @Column(name = "pinned_at")
    private LocalDateTime pinnedAt;

    public static Notice create(StudyGroup group, Member writer, String title, String content, boolean pinned) {

        // 작성 제한: CLOSED 불가)
        if (group.getOperationStatus() == OperationStatus.CLOSED) throw new IllegalStateException("종료된 스터디는 공지 작성 불가");

        Notice notice = new Notice();
        notice.studyGroup = group;
        notice.writer = writer;
        notice.title = title;
        notice.content = content;
        notice.setPinned(pinned);
        return notice;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
        this.pinnedAt = pinned ? java.time.LocalDateTime.now() : null;
    }
}