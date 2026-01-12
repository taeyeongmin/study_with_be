package com.ty.study_with_be.member.presentation.res;

public record MemberInfoRes(
        Long memberId,
        String nickname,
        String email
) {}
