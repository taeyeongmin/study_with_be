package com.ty.study_with_be.member.service.signup;

import com.ty.study_with_be.member.domain.model.Member;
import org.springframework.stereotype.Service;

@Service("KAKAO")
public class kakaoSignup implements SignupService {

    @Override
    public void validate(SignupReq signupReq) {
        System.err.println(">>>>> kakaoValid");
    }

    @Override
    public Member createMemberEntity(SignupReq signupReq) {
        return Member.createSocialMember(signupReq);
    }
}
