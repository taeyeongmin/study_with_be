package com.ty.study_with_be.notification.presentation.command;

import com.ty.study_with_be.notification.application.command.usecase.NotificationReadAllUseCase;
import com.ty.study_with_be.notification.application.command.usecase.NotificationReadUseCase;
import com.ty.study_with_be.notification.infra.sse.SseEmitterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "스터디 그룹 알림")
@RequestMapping("/api/study_group/notification")
public class NotificationController {

    private final SseEmitterRegistry sseEmitterRegistry;
    private final NotificationReadAllUseCase notificationReadAllUseCase;
    private final NotificationReadUseCase notificationReadUseCase;

    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "알림 읽음 처리",
            description = """
                    ## 기능 설명
                    - 나에게 온 읽지 않음 알림을 모두 읽음 처리한다(단건).
                    ---
                    ## 상세 설명
                    - 이미 읽은 알림은 처리할 수 없다.
                    ---
                  
                    """
    )
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> read(@AuthenticationPrincipal User userPrincipal, @PathVariable Long notificationId) {

        Long memberId = Long.valueOf(userPrincipal.getUsername());
        notificationReadUseCase.read(memberId,notificationId);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "알림 일괄 읽음 처리",
            description = """
                    ## 기능 설명
                    - 나에게 온 읽지 않음 알림을 모두 읽음 처리한다.
                    ---
                    ## 상세 설명
                    - 이미 읽은 알림은 처리할 수 없다.
                    ---
                  
                    """
    )
    @PatchMapping("/read_all")
    public ResponseEntity<Void> readAll(@AuthenticationPrincipal User userPrincipal) {

        Long memberId = Long.valueOf(userPrincipal.getUsername());
        notificationReadAllUseCase.readAll(memberId);

        return ResponseEntity.ok().build();
    }
    
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@AuthenticationPrincipal User userPrincipal) {

        Long memberId = Long.valueOf(userPrincipal.getUsername());
        log.info("[SSE][CONNECT] memberId={}", memberId);
        // 60분 유지
        return sseEmitterRegistry.register(memberId, 60L * 60 * 1000);
    }

}
