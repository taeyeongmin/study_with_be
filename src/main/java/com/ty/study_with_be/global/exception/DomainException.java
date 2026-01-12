package com.ty.study_with_be.global.exception;


import com.ty.study_with_be.global.error.ErrorCode;

public class DomainException extends BaseException {
    public DomainException(ErrorCode code) {
        super(code);
    }
}