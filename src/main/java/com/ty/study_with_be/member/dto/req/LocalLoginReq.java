package com.ty.study_with_be.member.dto.req;

public record LocalLoginReq(
        String username,
        String password
) {
}