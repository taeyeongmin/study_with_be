package com.ty.study_with_be.member.domain.model;

import com.ty.study_with_be.auth.presentation.req.SignupReq;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "member",
        uniqueConstraints = {
                // 로그인 식별자 유니크 보장
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
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", length = 20)
    private AuthType authType;

    /**
     * 소셜 로그인 고유 식별자
     * - SOCIAL : provider 응답 id (kakao id, google sub 등)
     */
    @Column(name = "provider_user_id", length = 100)
    private String providerUserId;

    /**
     * LOCAL 로그인 전용
     * - SOCIAL 로그인 사용자는 NULL
     */
    @Column(length = 255)
    private String password;

    /* =======================
       LOCAL 로그인 전용
       ======================= */
    @Column(length = 50)
    private String loginId;

    /* =======================
       회원 프로필
       ======================= */

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(length = 100)
    private String email;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    /* =======================
       생성 메서드
       ======================= */
    /**
     * ID / PW 회원가입
     */
    public static Member createLocalMember(
            String loginId
            , String password
            , String nickname
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
    public static Member createSocialMember(
            SignupReq signupReq
    ) {
        Member member = new Member();
        member.authType = signupReq.getAuthType();
        member.providerUserId = signupReq.getProviderUserId();
        member.nickname = signupReq.getNickname();
        return member;
    }

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
        validateLoginMethod();
    }

    void validateLoginMethod() {

        /*if (authType == AuthType.LOCAL) {
            if (loginId == null || password == null) {
                throw new IllegalStateException("LOCAL 로그인은 email/password가 필수입니다.");
            }
        } else {
            if (password != null) {
                throw new IllegalStateException("SOCIAL 로그인은 password를 가질 수 없습니다.");
            }
        }*/
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        validateLoginMethod();
    }

    void passwordMatch(String inputPassword) {

    }
}
