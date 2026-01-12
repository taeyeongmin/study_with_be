package com.ty.study_with_be.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 4xx
    HEADER_TYPE_ERROR(HttpStatus.UNAUTHORIZED, "error.header.type"),
    NOT_FOUND_HEADER(HttpStatus.UNAUTHORIZED, "error.header.not_found"),

    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "error.validation.failed"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "error.auth.forbidden"),

    // 5xx
    EXTERNAL_API_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "error.external.timeout"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "error.db"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "error.internal");

    private final HttpStatus httpStatus;
    private final String messageKey;
}