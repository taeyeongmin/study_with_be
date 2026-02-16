package com.ty.study_with_be.global.security.token;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuth2TicketPayload {
    private String accessToken;
    private Long memberId;
    private String nickname;
}
