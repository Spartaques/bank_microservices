package com.andriibashuk.userauthservice.exception;

import org.springframework.http.HttpStatus;

public class WrongRoleIdsException extends BaseHttpException {
    public WrongRoleIdsException(String message, HttpStatus httpStatus, String errorCode) {
        super(message, httpStatus, errorCode);
    }
}
