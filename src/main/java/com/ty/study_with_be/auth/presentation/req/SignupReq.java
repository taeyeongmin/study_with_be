package com.ty.study_with_be.auth.presentation.req;

import com.ty.study_with_be.member.domain.model.AuthType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupReq {

    @NotBlank
    private String loginId;
    @NotBlank
    private String password;
    private String providerUserId;
    @NotBlank
    private String nickname;
    private AuthType authType;
}


