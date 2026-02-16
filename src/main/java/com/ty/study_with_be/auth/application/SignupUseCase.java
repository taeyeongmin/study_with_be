package com.ty.study_with_be.auth.application;

import com.ty.study_with_be.auth.presentation.req.SignupReq;

public interface SignupUseCase {
    void register(SignupReq signupReq);
}
