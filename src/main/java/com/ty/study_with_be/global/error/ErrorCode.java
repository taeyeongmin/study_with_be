package com.ty.study_with_be.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 40X
    NOT_FOUND(HttpStatus.NOT_FOUND, "error.not_found"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "error.auth.unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "error.auth.forbidden"),
    INVALID_JSON(HttpStatus.BAD_REQUEST, "error.invalid.json"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "error.validation.required"),


    // 422
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "error.auth.duplicate_login_id"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "error.auth.invalid_credentials"),

    TOO_MANY_CREATE_GROUP(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.too_many_create_group"),
    DUPLICATE_GROUP_TITLE(HttpStatus.CONFLICT, "error.study_group.duplicate_group_title"),

    INVALID_STUDY_CAPACITY(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.invalid_capacity"),
    OFFLINE_STUDY_REGION_REQUIRED(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.offline_region_required"),
    STUDY_OWNER_REQUIRED(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.owner_required"),
    DUPLICATE_STUDY_OWNER(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.duplicate_owner"),
    NOT_GROUP_OWNER(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.not_group_owner"),
    NOT_RECRUITING(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.not_recruiting"),
    NOT_ONLY_OWNER(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.not_only_owner"),




    // 5xx
    EXTERNAL_API_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "error.external.timeout"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "error.db"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "error.internal");

    private final HttpStatus httpStatus;
    private final String messageKey;
}
