package com.ty.study_with_be.global.security.token;

import java.time.Duration;
import java.util.Optional;

/**
 * oAuth 로그인 완료 후 임시 토큰을 발행하고 우리 서비스의 accessToken으로 교환하기 위한 인터페이스
 */
public interface OAuth2ExchangeTokenStore {

    void save(String ticket, OAuth2TicketPayload payload, Duration ttl);

    /**
     * 단발성: 조회 성공 시 제거
     */
    Optional<OAuth2TicketPayload> consume(String ticket);
}
