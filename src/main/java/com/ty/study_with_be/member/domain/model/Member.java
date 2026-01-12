package com.ty.study_with_be.member.domain.model;

import com.ty.study_with_be.member.presentation.req.SignupReq;
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
                )
        },
        indexes = {
                @Index(name = "idx_member_username", columnList = "username"),
                @Index(name = "idx_member_email", columnList = "email")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", nullable = false, length = 20)
    private AuthType authType;

    /**
     * 로그인 고유 식별자
     * - LOCAL  : username
     * - SOCIAL : provider 응답 id (kakao id, google sub 등)
     */
    @Column(name = "provider_user_id", nullable = false, length = 100)
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
    private String username;

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

    /* =======================
       생성 메서드
       ======================= */
    /**
     * ID / PW 회원가입
     */
    public static Member createLocalMember(
            SignupReq signupReq
    ) {
        Member member = new Member();
        member.authType = AuthType.LOCAL;
        member.providerUserId = signupReq.getLoginId();
        member.username = signupReq.getName();
        member.password = signupReq.getPassword();
        member.nickname = signupReq.getNickname();
        member.email = signupReq.getEmail();
        return member;
    }

    /**
     * 소셜 회원가입
     */
    public static Member createSocialMember(
            SignupReq signupReq
    ) {
        if (signupReq.getAuthType() == AuthType.LOCAL) {
            throw new IllegalArgumentException("LOCAL provider는 이 메서드를 사용할 수 없습니다.");
        }

        Member member = new Member();
        member.authType = signupReq.getAuthType();
        member.providerUserId = signupReq.getProviderUserId();
        member.nickname = signupReq.getNickname();
        member.email = signupReq.getEmail();
        return member;
    }

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
        validateLoginMethod();
    }

    void validateLoginMethod() {

        if (authType == AuthType.LOCAL) {
            if (username == null || password == null) {
                throw new IllegalStateException("LOCAL 로그인은 username/password가 필수입니다.");
            }
        } else {
            if (password != null) {
                throw new IllegalStateException("SOCIAL 로그인은 password를 가질 수 없습니다.");
            }
        }
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        validateLoginMethod();
    }
}
