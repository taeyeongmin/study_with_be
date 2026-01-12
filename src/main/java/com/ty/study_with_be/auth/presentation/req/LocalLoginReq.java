package com.ty.study_with_be.auth.presentation.req;

public record LocalLoginReq(
        String username,
        String password
) {
}