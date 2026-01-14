package com.ty.study_with_be.global.exception;

import com.ty.study_with_be.global.error.ErrorCode;

public class InvalidCredentialsException extends DomainException {

    public InvalidCredentialsException() {
        super(ErrorCode.INVALID_CREDENTIALS);
    }
}
