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
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "error.auth.duplicate_nickname"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "error.auth.invalid_credentials"),

    TOO_MANY_CREATE_GROUP(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.too_many_create_group"),
    DUPLICATE_GROUP_TITLE(HttpStatus.CONFLICT, "error.study_group.duplicate_group_title"),

    INVALID_STUDY_CAPACITY(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.invalid_capacity"),
    OFFLINE_STUDY_REGION_REQUIRED(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.offline_region_required"),
    STUDY_OWNER_REQUIRED(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.owner_required"),
    DUPLICATE_STUDY_OWNER(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.duplicate_owner"),
    NOT_GROUP_OWNER(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.not_group_owner"),
    NOT_GROUP_MEMBER(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.not_group_member"),
    NOT_RECRUITING(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.not_recruiting"),
    DEADLINE_EXCEEDED(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.deadline_exceeded"),
    NOT_ONLY_OWNER(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.not_only_owner"),
    CAPACITY_EXCEEDED(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.capacity_exceeded"),
    STUDY_GROUP_NOT_MANAGER(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.not_manager"),
    OWNER_CANNOT_LEAVE(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.owner_cannot_leave"),
    CLOSE_STUDY_CANNOT_LEAVE(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.close_study_cannot_leave"),
    CLOSE_STUDY_CANNOT_PROC(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.close_study_cannot_proc"),
    CANNOT_SELF_KICK(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.cannot_self_kick"),
    CANNOT_SELF_PROC(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.cannot_self_proc"),

    ALREADY_JOINED(HttpStatus.UNPROCESSABLE_ENTITY, "error.join_request.already_joined"),
    ALREADY_JOINED_MEMBER(HttpStatus.UNPROCESSABLE_ENTITY, "error.study_group.already_joined_member"),
    DUPLICATE_REQUEST(HttpStatus.UNPROCESSABLE_ENTITY, "error.join_request.duplicate_request"),
    HAS_NOT_PERMISSION(HttpStatus.FORBIDDEN, "error.join_request.has_not_permission"),
    REQUEST_NOT_PENDING(HttpStatus.UNPROCESSABLE_ENTITY, "error.join_request.not_pending"),
    INVALID_REQUEST_PRECESS_STATUS(HttpStatus.UNPROCESSABLE_ENTITY, "error.join_request.invalid_precess_status"),
    NOT_REQUEST_OWNER(HttpStatus.UNPROCESSABLE_ENTITY, "error.join_request.not_request_owner"),

    ALREADY_READ(HttpStatus.UNPROCESSABLE_ENTITY, "error.notification.already_read"),
    NOT_RECIPIENT_MEMBER(HttpStatus.UNPROCESSABLE_ENTITY, "error.notification.not_recipient_member"),


    // 5xx
    EXTERNAL_API_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "error.external.timeout"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "error.db"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "error.internal");

    private final HttpStatus httpStatus;
    private final String messageKey;
}
