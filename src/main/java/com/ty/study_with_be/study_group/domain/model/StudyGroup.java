package com.ty.study_with_be.study_group.domain.model;

import com.ty.study_with_be.global.entity.BaseTimeEntity;
import com.ty.study_with_be.study_group.domain.model.enums.SchedulingType;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import com.ty.study_with_be.study_group.domain.model.enums.StudyStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(
        name = "study_group",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_studygroup_owner_title",
                columnNames = {"owner_id", "title"}
        ),
        indexes = {
                @Index(name = "idx_study_group_status", columnList = "status"),
                @Index(name = "idx_study_group_created_at", columnList = "created_at"),
                @Index(name = "idx_study_group_category", columnList = "category"),
                @Index(name = "idx_study_group_region", columnList = "region")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studyGroupId;

    @Column(name = "creat_member_Id", nullable = false, length = 60)
    private Long creatMemberId;

    @Column(name = "title", nullable = false, length = 60)
    private String title;

    @Column(name = "category", nullable = false, length = 30)
    private String category; // 초기엔 문자열(추후 Category Entity/Enum 확장)

    @Column(name = "topic", nullable = false, length = 60)
    private String topic;

    @Column(name = "region", length = 60)
    private String region;

    @Enumerated(EnumType.STRING)
    @Column(name = "study_mode", nullable = false, length = 10)
    private StudyMode studyMode;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "current_count", nullable = false)
    private int currentCount;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 15)
    private StudyStatus status;

    @Column(name = "apply_deadline_at")
    private LocalDate applyDeadlineAt;

    @Enumerated(EnumType.STRING)
    private SchedulingType schedulingType;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "study_group_schedule",
            joinColumns = @JoinColumn(name = "study_group_id")
    )
    private Set<StudySchedule> schedules;

    public static StudyGroup create(
            String title,
            String category,
            String topic,
            String region,
            StudyMode studyMode,
            int capacity,
            String description,
            LocalDate applyDeadlineAt,
            Set<StudySchedule> schedules
    ) {
        if (capacity < 1) throw new IllegalArgumentException("정원은 1 이상이어야 합니다.");

        StudyGroup g = new StudyGroup();
        g.title = title;
        g.category = category;
        g.topic = topic;
        g.region = region;
        g.studyMode = studyMode;
        g.capacity = capacity;
        g.currentCount = 1; // 방장 포함
        g.description = description;
        g.status = StudyStatus.RECRUITING;
        g.applyDeadlineAt = applyDeadlineAt;
        g.schedules = schedules;
        return g;
    }

    public void changeStatus(StudyStatus newStatus) {
        // 정책(간단 버전): CLOSED/SUSPENDED 후 되돌리기 여부는 추후 정책화
        this.status = newStatus;
    }

    public void increaseMemberCount() {
        if (this.currentCount >= this.capacity) {

            // 가득 찼다면 RECRUIT_END로 간주
            this.status = StudyStatus.RECRUIT_END;
            throw new IllegalStateException("정원이 초과되어 승인할 수 없습니다.");
        }
        this.currentCount++;
        if (this.currentCount == this.capacity) {
            this.status = StudyStatus.RECRUIT_END;
        }
    }

    public void decreaseMemberCount() {
        if (this.currentCount <= 1) throw new IllegalStateException("현재 인원 감소 불가");
        this.currentCount--;
        // 인원이 빠지면 다시 모집중 전환 가능한 정책(방장 전환) - 여기선 자동 전환 안함
    }

    public void validateAccessible() {
        if (this.status == StudyStatus.SUSPENDED) {
            throw new IllegalStateException("비활성 스터디는 접근할 수 없습니다.");
        }
    }

    public void validateCanApply() {
        if (this.status != StudyStatus.RECRUITING) {
            throw new IllegalStateException("모집중 상태에서만 가입 신청이 가능합니다.");
        }
        if (applyDeadlineAt != null && LocalDate.now().isAfter(applyDeadlineAt)) {
            throw new IllegalStateException("가입 신청 마감일이 지났습니다.");
        }
    }

    public void validateCanEdit(boolean isLeader) {
        if (!isLeader) throw new IllegalStateException("방장만 수정할 수 있습니다.");
        if (this.status == StudyStatus.CLOSED) throw new IllegalStateException("종료된 스터디는 수정할 수 없습니다.");
    }

    public void changeCapacity(int newCapacity) {
        if (newCapacity < this.currentCount) {
            throw new IllegalArgumentException("정원은 현재 인원 이상이어야 합니다.");
        }
        this.capacity = newCapacity;
        // 정원이 늘어나면 RECRUIT_END -> RECRUITING 가능(정책에 따라 방장이 상태 변경)
    }
}