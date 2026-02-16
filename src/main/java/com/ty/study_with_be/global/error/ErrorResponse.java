package com.ty.study_with_be.global.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Builder
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final String timestamp;
    private final String error;
    private final String errorCode;
    private final String message;
    private final String path;

    public static ErrorResponse of(ErrorCode code, String message, String path) {
        return ErrorResponse.builder()
                .timestamp(OffsetDateTime.now().toString())
                .error(code.getHttpStatus().getReasonPhrase())
                .errorCode(code.name())
                .message(message)
                .path(path)
                .build();
    }
}