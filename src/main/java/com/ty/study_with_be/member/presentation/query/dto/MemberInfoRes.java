package com.ty.study_with_be.member.presentation.query.dto;

public record MemberInfoRes(
        Long memberId,
        String nickname,
        String email,
        String loginType
) {}
