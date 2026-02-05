package com.ty.study_with_be.study_notice.domain.model;

import com.ty.study_with_be.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("공지 ID")
    private Long noticeId;

    @Column(name = "study_group_id", nullable = false)
    @Comment("스터디 그룹 ID")
    private Long studyGroupId;

    @Column(name = "created_id", nullable = false)
    @Comment("작성자 ID")
    private Long writerId;

    @Column(name = "title", nullable = false, length = 80)
    @Comment("제목")
    private String title;

    @Column(name = "content", nullable = false)
    @Comment("내용")
    private String content;

    @Column(name = "pinned", nullable = false)
    @Comment("상단 고정 여부")
    private boolean pinned;

    @Column(name = "updated_id")
    @Comment("수정자 ID")
    private Long updatedId;

    public static StudyNotice create(Long studyGroupId, Long writerId, String title, String content, boolean pinned) {

        StudyNotice studyNotice = new StudyNotice();
        studyNotice.studyGroupId = studyGroupId;
        studyNotice.writerId = writerId;
        studyNotice.updatedId = writerId;
        studyNotice.title = title;
        studyNotice.content = content;
        studyNotice.pinned = pinned;
        return studyNotice;
    }

    public void update(String title, String content, Long updatedId, boolean pinned) {
        this.title = title;
        this.content = content;
        this.updatedId = updatedId;
        this.pinned = pinned;
    }
}
