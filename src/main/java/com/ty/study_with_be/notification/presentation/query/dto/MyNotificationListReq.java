package com.ty.study_with_be.notification.presentation.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
@Schema(description = "My notification list query")
public class MyNotificationListReq {

    @Schema(
            description = "Read filter (ALL / UNREAD / READ)",
            example = "ALL",
            defaultValue = "ALL"
    )
    private MyNotificationReadFilter readFilter = MyNotificationReadFilter.ALL;

    @Schema(
            description = "Page number (0-based)",
            example = "0",
            defaultValue = "0"
    )
    @Min(0)
    private int page = 0;

    @Schema(
            description = "Page size",
            example = "20",
            defaultValue = "20"
    )
    @Min(1)
    private int size = 20;
}
