package com.ty.study_with_be.study_group.domain.model;

import com.ty.study_with_be.global.entity.BaseTimeEntity;
import com.ty.study_with_be.study_group.domain.model.enums.StudyRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(
    name = "study_member",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_study_member", columnNames = {"study_group_id", "member_id"})
    },
    indexes = {
        @Index(name = "idx_study_member_study", columnList = "study_group_id"),
        @Index(name = "idx_study_member_member", columnList = "member_id"),
        @Index(name = "idx_study_member_role", columnList = "role")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyMember extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studyMemberId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "study_group_id", nullable = false)
    private StudyGroup studyGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 10)
    private StudyRole role;

    public boolean hasManageRole(){
        return role == StudyRole.LEADER || role == StudyRole.MANAGER;
    }

    protected boolean isSameMemberByMemberId(Long memberId) {
        return memberId.equals(this.memberId);
    }

    protected boolean isSameMemberByStudyMemberId(Long studyMemberId) {
        return studyMemberId.equals(this.studyMemberId);
    }

    public static StudyMember createLeader(StudyGroup group, Long memberId) {
        StudyMember sm = new StudyMember();
        sm.studyGroup = group;
        sm.memberId = memberId;
        sm.role = StudyRole.LEADER;
        return sm;
    }

    public static StudyMember createMember(StudyGroup group, Long memberId) {
        StudyMember sm = new StudyMember();
        sm.studyGroup = group;
        sm.memberId = memberId;
        sm.role = StudyRole.MEMBER;
        return sm;
    }

    protected void changeRole(StudyRole role) {

        this.role = role;
    }

    public boolean isLeader() { return this.role == StudyRole.LEADER; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StudyMember that = (StudyMember) o;
        return Objects.equals(studyMemberId, that.studyMemberId) && Objects.equals(memberId, that.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studyMemberId, memberId);
    }

    public boolean canKick(StudyMember targetMember) {

        return this.role.canKick(targetMember.role);
    }
}