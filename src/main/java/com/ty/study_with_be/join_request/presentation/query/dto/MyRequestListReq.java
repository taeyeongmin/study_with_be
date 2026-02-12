package com.ty.study_with_be.join_request.presentation.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "신청 목록 조회 req DTO")
public class MyRequestListReq {

    @Schema(
            description = "Request status filter (PENDING / APPROVED / REJECTED / CANCELED / ALL)",
            example = "PENDING",
            defaultValue = "PENDING"
    )
    private MyRequestStatusFilter statusFilter = MyRequestStatusFilter.PENDING;

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
