package com.ty.study_with_be.study_group.domain.model;

import com.ty.study_with_be.global.entity.BaseTimeEntity;
import com.ty.study_with_be.member.domain.model.Member;
import com.ty.study_with_be.study_group.domain.model.enums.StudyRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "study_group_id", nullable = false)
    private StudyGroup studyGroup;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 10)
    private StudyRole role;

    public static StudyMember leader(StudyGroup group, Member member) {
        StudyMember sm = new StudyMember();
        sm.studyGroup = group;
        sm.member = member;
        sm.role = StudyRole.LEADER;
        return sm;
    }

    public void promoteToManager() {
        if (this.role == StudyRole.LEADER) return; // 리더는 그대로
        this.role = StudyRole.MANAGER;
    }

    public void demoteToMember() {
        if (this.role == StudyRole.LEADER) {
            throw new IllegalStateException("방장은 MEMBER로 변경할 수 없습니다.");
        }
        this.role = StudyRole.MEMBER;
    }

    public boolean isLeader() { return this.role == StudyRole.LEADER; }
    public boolean isManager() { return this.role == StudyRole.MANAGER; }
}