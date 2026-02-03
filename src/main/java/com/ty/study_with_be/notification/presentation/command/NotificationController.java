package com.ty.study_with_be.notification.presentation.command;

import com.ty.study_with_be.notification.infra.sse.SseEmitterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study_group/notification")
public class NotificationController {

    private final SseEmitterRegistry sseEmitterRegistry;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@AuthenticationPrincipal User userPrincipal) {

        Long memberId = Long.valueOf(userPrincipal.getUsername());

        // 60분 유지
        return sseEmitterRegistry.register(memberId, 60L * 60 * 1000);
    }

}
