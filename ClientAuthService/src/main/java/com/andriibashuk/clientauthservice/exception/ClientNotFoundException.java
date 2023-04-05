package com.andriibashuk.clientauthservice.exception;

import org.springframework.http.HttpStatus;

public class ClientNotFoundException extends BaseHttpException{
    public ClientNotFoundException(String message, HttpStatus httpStatus, String errorCode) {
        super(message, httpStatus, errorCode);
    }
}
