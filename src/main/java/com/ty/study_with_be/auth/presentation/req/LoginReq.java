package com.ty.study_with_be.auth.presentation.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginReq {

    @NotBlank
    private String loginId;
    @NotBlank
    private String password;
}
