package com.ty.study_with_be.member.service.signup;

import com.ty.study_with_be.auth.presentation.req.SignupReq;
import com.ty.study_with_be.member.domain.model.Member;
import org.springframework.stereotype.Service;

@Service("LOCAL")
public class LocalSignup implements SignupService {

    @Override
    public void validate(SignupReq signupReq) {
        System.err.println(">>>>> LocalValid");
    }

    @Override
    public Member createMemberEntity(SignupReq signupReq) {
        return Member.createLocalMember(signupReq.getLoginId(),signupReq.getPassword(), signupReq.getNickname());
    }
}
