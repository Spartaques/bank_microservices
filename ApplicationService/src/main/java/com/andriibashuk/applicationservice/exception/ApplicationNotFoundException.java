package com.andriibashuk.applicationservice.exception;

import org.springframework.http.HttpStatus;

public class ApplicationNotFoundException extends BaseHttpException {
    public ApplicationNotFoundException(String message, HttpStatus httpStatus, String errorCode) {
        super(message, httpStatus, errorCode);
    }
}
