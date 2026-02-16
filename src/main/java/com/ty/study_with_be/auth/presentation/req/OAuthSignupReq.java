package com.ty.study_with_be.auth.presentation.req;

import com.ty.study_with_be.member.domain.model.AuthType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OAuthSignupReq {

    @NotNull
    private AuthType authType;
    @NotBlank
    private String providerUserId;
    @NotBlank
    private String nickname;
}


