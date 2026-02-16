package com.ty.study_with_be.auth.application;

import com.ty.study_with_be.auth.presentation.req.LoginReq;

public interface LoginUseCase {

    String login(LoginReq loginReq);
}
