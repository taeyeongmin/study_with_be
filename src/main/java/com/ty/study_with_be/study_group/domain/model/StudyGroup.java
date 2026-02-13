package com.ty.study_with_be.study_group.domain.model;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.ty.study_with_be.global.entity.BaseTimeEntity;
import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.event.domain.DomainEvent;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.study_group.domain.event.*;
import com.ty.study_with_be.study_group.domain.model.enums.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    @Comment("스터디 그룹 PK")
    private Long studyGroupId;

    @Column(name = "owner_id", nullable = false)
    @Comment("방장 회원PK")
    private Long ownerId;

    @Column(name = "title", nullable = false, length = 60)
    @Comment("스터디 타이틀")
    private String title;

    @Column(name = "category", nullable = false, length = 30)
    @Comment("카테고리")
    private String category;

    @Column(name = "topic", nullable = false, length = 60)
    @Comment("주제")
    private String topic;

    @Column(name = "region", length = 60)
    @Comment("지역")
    private String region;

    @Enumerated(EnumType.STRING)
    @Column(name = "study_mode", nullable = false, length = 10)
    @Comment("온/오프라인")
    private StudyMode studyMode;

    @Column(name = "capacity", nullable = false)
    @Comment("정원")
    private int capacity;

    @Column(name = "current_count", nullable = false)
    @Comment("현재 인원")
    private int currentCount;

    @Lob
    @Column(name = "description", nullable = false)
    @Comment("설명")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "recruit_status", nullable = false, length = 15)
    @Comment("모집 상태")
    private RecruitStatus recruitStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_status", nullable = false, length = 15)
    @Comment("운영 상태")
    private OperationStatus operationStatus;

    @Column(name = "apply_deadline_at")
    @Comment("신청 마감일")
    private LocalDate applyDeadlineAt;

    @Enumerated(EnumType.STRING)
    @Comment("일정 유형")
    private SchedulingType schedulingType;

    @ElementCollection(fetch = LAZY)
    @CollectionTable(
        name = "study_group_schedule",
        joinColumns = @JoinColumn(name = "study_group_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Comment("진행 예정 요일")
    private Set<DayOfWeek> schedules;

    @OneToMany(mappedBy = "studyGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StudyMember> members = new HashSet<>();

    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    @Column(name = "closed_at")
    @Comment("운영 종료일")
    private LocalDateTime closedAt;

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
        Long memberId
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
        group.operationStatus = OperationStatus.ONGOING;
        group.applyDeadlineAt = applyDeadlineAt;
        group.schedules = schedules;
        group.ownerId = memberId;

        group.addReader(memberId);

        return group;
    }


    public void joinMember(Long requesterId, Long currentMemberId) {

        validJoinMember(currentMemberId);

        StudyMember newMember = StudyMember.createMember(this, requesterId);

        if (this.members.contains(newMember)) throw new DomainException(ErrorCode.ALREADY_JOINED_MEMBER);

        this.members.add(newMember);
        increaseMemberCount();
    }

    protected void validJoinMember(Long currentMemberId) {

        StudyMember studyMember = findStudyMemberByMemberId(currentMemberId);

        if (!studyMember.hasManageRole()) throw new DomainException(ErrorCode.HAS_NOT_PERMISSION);

        this.validateRecruitable();
    }

    public void updateInfo(String title, String category, String topic, String region, StudyMode studyMode, int capacity, String description, LocalDate applyDeadlineAt, Set<DayOfWeek> schedules, Long memberId) {

        if (!isLeader(memberId)) throw new DomainException(ErrorCode.NOT_GROUP_OWNER);

        if (studyMode == StudyMode.OFFLINE && StringUtils.isBlank(region))
            throw new DomainException(ErrorCode.OFFLINE_STUDY_REGION_REQUIRED);

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

    // TODO: 테스트 필요
    public void updateOperationInfo(int capacity, StudyMode studyMode, SchedulingType schedulingType, Set<DayOfWeek> schedules, Long memberId) {

        if (!isLeader(memberId)) throw new DomainException(ErrorCode.NOT_GROUP_OWNER);

        if (studyMode == StudyMode.OFFLINE && StringUtils.isBlank(region))
            throw new DomainException(ErrorCode.OFFLINE_STUDY_REGION_REQUIRED);

        this.capacity = capacity;
        this.studyMode = studyMode;
        this.schedulingType = schedulingType;
        this.schedules = schedules;
    }


    public void validDelete(Long currentMemberId) {

        if (!isLeader(currentMemberId))
            throw new DomainException(ErrorCode.NOT_GROUP_OWNER);

        if (currentCount != 1) {
            throw new DomainException(ErrorCode.NOT_ONLY_OWNER);
        }
    }

    public boolean isFull() {
        return this.capacity == this.currentCount;
    }

    /**
     * 모집 상태: 모집 중이란 판단을 아래의 값을 통해 최종 판단
     * - RecruitStatus (수동으로 방장이 변경 가능)
     * - 정원 수
     * - 모집 마감일
     * @return
     */
    public void validateRecruitable() {

        RecruitAvailability availability = getRecruitAvailability();

        if (availability != RecruitAvailability.OPEN) {
            switch (availability) {
                case CLOSED_FULL -> throw new DomainException(ErrorCode.CAPACITY_EXCEEDED);
                case CLOSED_DEADLINE -> throw new DomainException(ErrorCode.DEADLINE_EXCEEDED);
                case CLOSED_MANUAL -> throw new DomainException(ErrorCode.NOT_RECRUITING);
            }
        }
    }

    private RecruitAvailability getRecruitAvailability() {

        if (this.recruitStatus != RecruitStatus.RECRUITING) {
            return RecruitAvailability.CLOSED_MANUAL;
        }

        if (this.applyDeadlineAt != null &&
                LocalDate.now().isAfter(this.applyDeadlineAt)) {
            return RecruitAvailability.CLOSED_DEADLINE;
        }

        if (isFull()) {
            return RecruitAvailability.CLOSED_FULL;
        }

        return RecruitAvailability.OPEN;
    }

    public StudyMember findStudyMemberByStudyMemberId(Long studyMemberId) {

        return this.members.stream()
            .filter(member -> member.isSameMemberByStudyMemberId(studyMemberId))
            .findFirst().orElseThrow(() -> new DomainException(ErrorCode.NOT_GROUP_MEMBER));
    }

    public StudyMember findStudyMemberByMemberId(Long memberId) {

        return this.members.stream()
            .filter(member -> member.isSameMemberByMemberId(memberId))
            .findFirst().orElseThrow(() -> new DomainException(ErrorCode.NOT_GROUP_MEMBER));
    }

    public void leave(Long memberId) {

        // StudyMember 조회
        StudyMember member = findStudyMemberByMemberId(memberId);
        // 검증
        validLeave(member);
        // members에서 제거
        removeMember(member);

        this.raise(MemberLeaveEvent.of(studyGroupId, memberId));
    }


    public void transferLeaderAndLeave(Long targetStudyMemberId, Long currentMemberId) {
        changeRole(targetStudyMemberId, currentMemberId, StudyRole.LEADER);
        leaderLeave(currentMemberId);

        this.raise(MemberLeaveEvent.of(studyGroupId, currentMemberId));
    }

    public void expelMember(Long targetMemberId, long currentMemberId) {

        // 현재 멤버 및 강퇴 대상 조회
        StudyMember currentMember = findStudyMemberByMemberId(currentMemberId);
        StudyMember targetMember = findStudyMemberByStudyMemberId(targetMemberId);

        // 정책 검증
        validExpelMember(targetMember, currentMember);

        removeMember(targetMember);

        this.raise(MemberKickEvent.of(studyGroupId, targetMember.getMemberId(), currentMemberId));

    }

    public void changeRole(Long targetStudyMemberId, Long currentMemberId, StudyRole role) {

        // studyMember 조회
        StudyMember targetMember = findStudyMemberByStudyMemberId(targetStudyMemberId);
        StudyMember currentMember = findStudyMemberByMemberId(currentMemberId);

        // 정책 검증
        validChangeRole(targetMember, currentMember);

        targetMember.changeRole(role);

        this.raise(ChangeRoleEvent.of(studyGroupId, targetMember.getMemberId(), currentMemberId));
    }

    public boolean isLeader(Long memberId) {
        return getLeader().getMemberId().equals(memberId);
    }

    /**
     * 이벤트를 꺼내면서 비운다(중복 발행 방지).
     */
    public List<DomainEvent> pullDomainEvents() {

        List<DomainEvent> events = new ArrayList<>(this.domainEvents);
        this.domainEvents.clear();

        return events;
    }

    private void addReader(Long memberId) {

        if (memberId == null) throw new DomainException(ErrorCode.STUDY_OWNER_REQUIRED);
        if (this.members.stream().anyMatch(StudyMember::isLeader)) throw new DomainException(ErrorCode.DUPLICATE_STUDY_OWNER);

        StudyMember leader = StudyMember.createLeader(this, memberId);
        this.members.add(leader);
    }

    private StudyMember getLeader() {
        return members.stream().filter(StudyMember::isLeader).findFirst().orElse(null);
    }

    private void increaseMemberCount() {
        this.currentCount += 1;
    }

    private void leaderLeave(Long memberId) {

        // StudyMember 조회
        StudyMember member = findStudyMemberByMemberId(memberId);

        // members에서 제거
        removeMember(member);
    }

    private void validChangeRole(StudyMember targetMember, StudyMember currentMember) {

        // 방장 체크
        if (!isLeader(currentMember.getMemberId())) throw new DomainException(ErrorCode.NOT_GROUP_OWNER);

        // 대상 변경 체크
        if (currentMember.equals(targetMember)) throw new DomainException(ErrorCode.CANNOT_SELF_PROC);

        // 진행 중인 스터디 상태 체크
        if (this.operationStatus == OperationStatus.CLOSED) throw new DomainException(ErrorCode.CLOSE_STUDY_CANNOT_PROC);
    }


    private void removeMember(StudyMember member) {

        this.members.remove(member);
        if (this.currentCount <= 1) throw new IllegalStateException("현재 인원 감소 불가");
        this.currentCount--;
        // 인원이 비면 다시 모집중으로 전환 가능성 고려(방장 전환) - 현재는 자동 전환 안 함
    }

    private void validLeave(StudyMember member) {

        if (member.isLeader()) throw new DomainException(ErrorCode.OWNER_CANNOT_LEAVE);
        if (this.operationStatus == OperationStatus.CLOSED) throw new DomainException(ErrorCode.CLOSE_STUDY_CANNOT_LEAVE);
    }

    private void validExpelMember(StudyMember targetMember, StudyMember currentMember) {

        // self 강퇴 검증
        if (currentMember.equals(targetMember)) throw new DomainException(ErrorCode.CANNOT_SELF_KICK);

        // 권한 체크
        if (!currentMember.canKick(targetMember)) throw new DomainException(ErrorCode.HAS_NOT_PERMISSION);

        // 스터디 상태 체크
        if (this.operationStatus == OperationStatus.CLOSED) throw new DomainException(ErrorCode.CLOSE_STUDY_CANNOT_LEAVE);
    }

    private void raise(DomainEvent event) {
        this.domainEvents.add(event);
    }


    public void endRecruitment(Long currentMemberId) {

        if (!isLeader(currentMemberId)) throw new DomainException(ErrorCode.NOT_GROUP_OWNER);
        if (this.recruitStatus != RecruitStatus.RECRUITING) throw new DomainException(ErrorCode.NOT_RECRUITING);

        this.recruitStatus = RecruitStatus.RECRUIT_END;
        this.raise(RecruitmentEndEvent.of(this.studyGroupId, currentMemberId));
    }

    public void resumeRecruitment(Long currentMemberId) {

        if (!isLeader(currentMemberId)) throw new DomainException(ErrorCode.NOT_GROUP_OWNER);
        if (this.recruitStatus == RecruitStatus.RECRUITING) throw new DomainException(ErrorCode.NOT_RECRUIT_END);
        if (isFull()) throw new DomainException(ErrorCode.CAPACITY_EXCEEDED);

        this.recruitStatus = RecruitStatus.RECRUITING;

        this.raise(RecruitmentResumeEvent.of(this.studyGroupId, currentMemberId));
    }

    public void endOperation(Long currentMemberId) {

        if (!isLeader(currentMemberId)) throw new DomainException(ErrorCode.NOT_GROUP_OWNER);
        if (this.recruitStatus == RecruitStatus.RECRUITING) throw new DomainException(ErrorCode.NOT_RECRUIT_END);

        this.operationStatus = OperationStatus.CLOSED;

        this.raise(OperationEndEvent.of(this.studyGroupId, currentMemberId));
    }
}
