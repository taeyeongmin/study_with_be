package com.ty.study_with_be.auth.presentation.res;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OAuth2TicketRes {
    String accessToken;
    Long memberId;
    String nickname;
}
