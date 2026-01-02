package com.ty.study_with_be.member.dto.req;

import com.ty.study_with_be.member.enums.AuthType;
import lombok.Data;

@Data
public class SignupReq {
    private String loginId;
    private String password;
    private String name;
    private String providerUserId;
    private String nickname;
    private String email;
    private AuthType authType;
}


