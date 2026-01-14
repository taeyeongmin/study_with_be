package com.ty.study_with_be.global.exception;


import com.ty.study_with_be.global.error.ErrorCode;

public class DuplicateLoginIdException extends DomainException{

    public DuplicateLoginIdException() {
        super(ErrorCode.DUPLICATE_LOGIN_ID);
    }
}
