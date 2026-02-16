package com.ty.study_with_be.auth.application;

import com.ty.study_with_be.auth.presentation.req.OAuthSignupReq;

public interface OAuthSignupUseCase {
    void register(OAuthSignupReq signupReq);
}
