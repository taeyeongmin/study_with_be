package com.ty.study_with_be.notification.infra.sse;

import com.ty.study_with_be.notification.presentation.command.dto.SseNotificationDto;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Component
public class NotificationSsePublisher {

    private final SseEmitterRegistry registry;

    public NotificationSsePublisher(SseEmitterRegistry registry) {
        this.registry = registry;
    }

    public void publish(Long recipientMemberId, SseNotificationDto dto) {

        for (SseEmitter emitter : registry.getEmitters(recipientMemberId)) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .id(String.valueOf(dto.id()))
                        .data(dto));
            } catch (IOException e) {
                // 실패 emitter는 registry에서 onError로 정리되지만,
                // send 도중 IOException이 뜨면 completion 콜백이 안 탈 수 있어 직접 완료 처리
                emitter.completeWithError(e);
            }
        }
    }
}