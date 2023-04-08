package com.andriibashuk.userauthservice.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseHttpException {
    public UserNotFoundException(String message, HttpStatus httpStatus, String errorCode) {
        super(message, httpStatus, errorCode);
    }
}
