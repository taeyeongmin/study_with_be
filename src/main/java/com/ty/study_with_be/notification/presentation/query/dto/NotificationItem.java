package com.ty.study_with_be.notification.presentation.query.dto;

import com.ty.study_with_be.global.event.domain.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotificationItem {

    @Schema(description = "알림 PK", example = "1")
    private Long id;

    @Schema(description = "수신자 회원 ID", example = "1")
    private Long recipientMemberId;

    @JsonIgnore
    @Schema(description = "이벤트 유형", hidden = true)
    private EventType type;

    @Schema(description = "이벤트 유형")
    private String eventTypeNm;

    @Schema(description = "메세지 내용")
    private String content;

    @Schema(description = "읽은 시간")
    private LocalDateTime readAt;

    @Schema(description = "생성 시간")
    private LocalDateTime createdAt;
}
