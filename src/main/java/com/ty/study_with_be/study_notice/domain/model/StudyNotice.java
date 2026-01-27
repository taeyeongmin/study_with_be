package com.ty.study_with_be.study_notice.domain.model;

import com.ty.study_with_be.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "study_notice",
    indexes = {
        @Index(name = "idx_notice_study", columnList = "study_group_id"),
        @Index(name = "idx_notice_created_at", columnList = "study_group_id, created_at")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyNotice extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    @Column(name = "study_group_id", nullable = false)
    private Long studyGroupId;

    @Column(name = "created_id", nullable = false)
    private Long writerId;

    @Column(name = "title", nullable = false, length = 80)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "pinned", nullable = false)
    private boolean pinned;

    @Column(name = "updated_id")
    private Long updatedId;

    public static StudyNotice create(Long studyGroupId, Long writerId, String title, String content, boolean pinned) {

        StudyNotice studyNotice = new StudyNotice();
        studyNotice.studyGroupId = studyGroupId;
        studyNotice.writerId = writerId;
        studyNotice.updatedId = writerId;
        studyNotice.title = title;
        studyNotice.content = content;
        studyNotice.setPinned(pinned);
        return studyNotice;
    }

    public void update(String title, String content, Long updatedId) {
        this.title = title;
        this.content = content;
        this.updatedId = updatedId;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
}