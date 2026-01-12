package com.ty.study_with_be.member.presentation.req;

public record LocalLoginReq(
        String username,
        String password
) {
}