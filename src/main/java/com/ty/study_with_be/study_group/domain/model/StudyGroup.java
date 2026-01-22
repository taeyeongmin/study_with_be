package com.ty.study_with_be.study_group.domain.model;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.ty.study_with_be.global.entity.BaseTimeEntity;
import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.member.domain.model.Member;
import com.ty.study_with_be.study_group.domain.model.enums.OperationStatus;
import com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus;
import com.ty.study_with_be.study_group.domain.model.enums.SchedulingType;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(
        name = "study_group",
        indexes = {
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

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

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
    @Column(name = "recruit_status", nullable = false, length = 15)
    private RecruitStatus recruitStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_status", nullable = false, length = 15)
    private OperationStatus operationStatus;

    @Column(name = "apply_deadline_at")
    private LocalDate applyDeadlineAt;

    @Enumerated(EnumType.STRING)
    private SchedulingType schedulingType;

    @ElementCollection(fetch = LAZY)
    @CollectionTable(
            name = "study_group_schedule",
            joinColumns = @JoinColumn(name = "study_group_id")

    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<DayOfWeek> schedules;

    @OneToMany(mappedBy = "studyGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StudyMember> members = new HashSet<>();
//
//    @OneToMany(mappedBy = "studyGroup", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<JoinRequest> joinRequests = new HashSet<>();
//
//    @OneToMany(mappedBy = "studyGroup", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Notice> notices = new HashSet<>();

    public static StudyGroup create(
            String title,
            String category,
            String topic,
            String region,
            StudyMode studyMode,
            int capacity,
            String description,
            LocalDate applyDeadlineAt,
            Set<DayOfWeek> schedules,
            Long memberId,
            Member member
    ) {
        if (capacity < 2) throw new DomainException(ErrorCode.INVALID_STUDY_CAPACITY);
        if (studyMode == StudyMode.OFFLINE && StringUtils.isBlank(region)) throw new DomainException(ErrorCode.OFFLINE_STUDY_REGION_REQUIRED);

        StudyGroup group = new StudyGroup();
        group.title = title;
        group.category = category;
        group.topic = topic;
        group.region = region;
        group.studyMode = studyMode;
        group.capacity = capacity;
        group.currentCount = 1; // 방장 포함
        group.description = description;
        group.recruitStatus = RecruitStatus.RECRUITING;
        group.operationStatus = OperationStatus.PREPARING;
        group.applyDeadlineAt = applyDeadlineAt;
        group.schedules = schedules;
        group.ownerId = memberId;

        group.addReader(member);

        return group;
    }

    private void addReader(Member member) {

        if (member == null) throw new DomainException(ErrorCode.STUDY_OWNER_REQUIRED);
        if (this.members.stream().anyMatch(StudyMember::isLeader)) throw new DomainException(ErrorCode.DUPLICATE_STUDY_OWNER);;

        StudyMember leader = StudyMember.leader(this, member);
        this.members.add(leader);
    }

    public void updateInfo(String title, String category, String topic, String region,  StudyMode studyMode, int capacity, String description, LocalDate applyDeadlineAt, Set<DayOfWeek> schedules) {

        if (studyMode == StudyMode.OFFLINE && StringUtils.isBlank(region))
            throw new DomainException(ErrorCode.OFFLINE_STUDY_REGION_REQUIRED);;

        this.title = title;
        this.category = category;
        this.topic = topic;
        this.region = region;
        this.studyMode = studyMode;
        this.capacity = capacity;
        this.description = description;
        this.applyDeadlineAt = applyDeadlineAt;
        this.schedules = schedules;
    }

    public void updateOperationInfo(int capacity, StudyMode studyMode, SchedulingType schedulingType, Set<DayOfWeek> schedules) {

        if (studyMode == StudyMode.OFFLINE && StringUtils.isBlank(region))
            throw new DomainException(ErrorCode.OFFLINE_STUDY_REGION_REQUIRED);

        this.capacity = capacity;
        this.studyMode = studyMode;
        this.schedulingType = schedulingType;
        this.schedules = schedules;
    }

    public boolean isOwner(Long loginMemberId) {
        return this.ownerId.equals(loginMemberId);
    }

    public void validDelete() {
        if (recruitStatus != RecruitStatus.RECRUITING) {
            throw new DomainException(ErrorCode.NOT_RECRUITING);
        }

        if (currentCount != 1) {
            throw new DomainException(ErrorCode.NOT_ONLY_OWNER);
        }
    }

    public boolean isFull() {
        return capacity == members.size();
    }

    public boolean isRecruiting() {
        return this.recruitStatus.equals(RecruitStatus.RECRUITING);
    }

//    public void updateBasicInfo() {
//        this.title = title;
//        this.category = category;
//        this.topic = topic;
//        this.region = region;
//        this.studyMode = studyMode;
//        this.capacity = capacity;
//        this.description = description;
//        this.applyDeadlineAt = applyDeadlineAt;
//        this.schedules = schedules;
//    }

//    public void increaseMemberCount() {
//        if (this.currentCount >= this.capacity) {
//
//            // 가득 찼다면 RECRUIT_END로 간주
//            this.status = RecruitStatus.RECRUIT_END;
//            throw new IllegalStateException("정원이 초과되어 승인할 수 없습니다.");
//        }
//        this.currentCount++;
//        if (this.currentCount == this.capacity) {
//            this.status = RecruitStatus.RECRUIT_END;
//        }
//    }
//
//    public void decreaseMemberCount() {
//        if (this.currentCount <= 1) throw new IllegalStateException("현재 인원 감소 불가");
//        this.currentCount--;
//        // 인원이 빠지면 다시 모집중 전환 가능한 정책(방장 전환) - 여기선 자동 전환 안함
//    }
//
//    public void validateAccessible() {
//        if (this.status == RecruitStatus.SUSPENDED) {
//            throw new IllegalStateException("비활성 스터디는 접근할 수 없습니다.");
//        }
//    }
//
//    public void validateCanApply() {
//        if (this.status != RecruitStatus.RECRUITING) {
//            throw new IllegalStateException("모집중 상태에서만 가입 신청이 가능합니다.");
//        }
//        if (applyDeadlineAt != null && LocalDate.now().isAfter(applyDeadlineAt)) {
//            throw new IllegalStateException("가입 신청 마감일이 지났습니다.");
//        }
//    }
//
//    public void validateCanEdit(boolean isLeader) {
//        if (!isLeader) throw new IllegalStateException("방장만 수정할 수 있습니다.");
//        if (this.status == RecruitStatus.CLOSED) throw new IllegalStateException("종료된 스터디는 수정할 수 없습니다.");
//    }
//
//    public void changeCapacity(int newCapacity) {
//        if (newCapacity < this.currentCount) {
//            throw new IllegalArgumentException("정원은 현재 인원 이상이어야 합니다.");
//        }
//        this.capacity = newCapacity;
//        // 정원이 늘어나면 RECRUIT_END -> RECRUITING 가능(정책에 따라 방장이 상태 변경)
//    }
//
//    public boolean isSuspended() {
//        return this.status == RecruitStatus.SUSPENDED;
//    }
}
