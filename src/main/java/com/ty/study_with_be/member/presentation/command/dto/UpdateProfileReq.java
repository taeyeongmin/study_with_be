package com.ty.study_with_be.member.presentation.command.dto;

import lombok.Data;

@Data
public class UpdateProfileReq{

    private String nickname;  // null이면 변경 안 함
    private String email;      // null이면 변경 안 함
}
