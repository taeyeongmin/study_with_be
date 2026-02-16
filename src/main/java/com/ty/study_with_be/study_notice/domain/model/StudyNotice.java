package com.ty.study_with_be.study_notice.domain.model;

import com.ty.study_with_be.global.entity.BaseTimeEntity;
import com.ty.study_with_be.global.event.domain.DomainEvent;
import com.ty.study_with_be.study_group.domain.event.MemberLeaveEvent;
import com.ty.study_with_be.study_notice.domain.event.CreateGroupNoticeEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

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

    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    /**
     * 이벤트를 꺼내면서 비운다(중복 발행 방지).
     */
    public List<DomainEvent> pullDomainEvents() {

        List<DomainEvent> events = new ArrayList<>(this.domainEvents);
        this.domainEvents.clear();

        return events;
    }

    public static StudyNotice create(Long studyGroupId, Long currentMemberId, String title, String content, boolean pinned) {

        StudyNotice studyNotice = new StudyNotice();
        studyNotice.studyGroupId = studyGroupId;
        studyNotice.writerId = currentMemberId;
        studyNotice.updatedId = currentMemberId;
        studyNotice.title = title;
        studyNotice.content = content;
        studyNotice.pinned = pinned;

        studyNotice.raise(CreateGroupNoticeEvent.of(studyGroupId, currentMemberId));

        return studyNotice;
    }

    public void update(String title, String content, Long updatedId, boolean pinned) {
        this.title = title;
        this.content = content;
        this.updatedId = updatedId;
        this.pinned = pinned;
    }

    private void raise(DomainEvent event) {
        this.domainEvents.add(event);
    }
}
