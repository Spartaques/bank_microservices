package com.andriibashuk.userauthservice.exception;

import org.springframework.http.HttpStatus;

public class WrongRolesCountException extends BaseHttpException {
    public WrongRolesCountException(String message, HttpStatus httpStatus, String errorCode) {
        super(message, httpStatus, errorCode);
    }
}
