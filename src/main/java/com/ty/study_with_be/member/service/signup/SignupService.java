package com.ty.study_with_be.member.service.signup;

import com.ty.study_with_be.member.domain.model.Member;

public interface SignupService {
    void validate(SignupReq signupReq);

    Member createMemberEntity(SignupReq signupReq);
}
