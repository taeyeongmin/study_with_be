package com.ty.study_with_be.member.domain.model;

import com.ty.study_with_be.auth.presentation.req.SignupReq;
import com.ty.study_with_be.global.entity.BaseTimeEntity;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "member",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_provider_user",
            columnNames = {"provider_type", "provider_user_id"}
        ),
        @UniqueConstraint(
            name = "uk_local_user",
            columnNames = {"login_id"}
        )
    },
    indexes = {
        @Index(name = "idx_member_login_id", columnList = "login_id"),
        @Index(name = "idx_member_email", columnList = "email")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    @Comment("회원 PK")
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", length = 20)
    @Comment("로그인 유형")
    private AuthType authType;

    @Column(name = "provider_user_id", length = 100)
    @Comment("소셜 로그인 고유 식별값")
    private String providerUserId;

    @Column(length = 255)
    @Comment("로컬 로그인 비밀번호")
    private String password;

    @Column(length = 50)
    @Comment("로그인 ID")
    private String loginId;

    @Column(nullable = false, length = 50)
    @Comment("닉네임")
    private String nickname;

    @Column(length = 100)
    @Comment("이메일")
    private String email;

    @Comment("삭제 시간")
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Comment("회원 권한")
    private Role role = Role.USER;

    /**
     * ID / PW 회원가입
     */
    public static Member createLocalMember(
        String loginId,
        String password,
        String nickname
    ) {
        if (StringUtils.isBlank(loginId)) throw new IllegalArgumentException("loginId is blank");
        if (StringUtils.isBlank(password)) throw new IllegalArgumentException("password is blank");
        if (StringUtils.isBlank(nickname)) throw new IllegalArgumentException("nickname is blank");

        Member member = new Member();
        member.loginId = loginId;
        member.password = password;
        member.nickname = nickname;
        member.email = loginId;
        return member;
    }

    /**
     * 소셜 회원가입
     */
    public static Member createSocialMember(SignupReq signupReq) {
        Member member = new Member();
        member.authType = signupReq.getAuthType();
        member.providerUserId = signupReq.getProviderUserId();
        member.nickname = signupReq.getNickname();
        return member;
    }

    @PrePersist
    void prePersist() {
        validateLoginMethod();
    }

    void validateLoginMethod() {

        /*if (authType == AuthType.LOCAL) {
            if (loginId == null || password == null) {
                throw new IllegalStateException("LOCAL 로그인은 email/password가 필수입니다.");
            }
        } else {
            if (password != null) {
                throw new IllegalStateException("SOCIAL 로그인은 password를 가지지 않습니다.");
            }
        }*/
    }

    @PreUpdate
    void preUpdate() {
        validateLoginMethod();
    }
}
