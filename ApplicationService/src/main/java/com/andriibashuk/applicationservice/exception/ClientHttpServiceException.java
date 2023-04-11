package com.andriibashuk.applicationservice.exception;

import org.springframework.http.HttpStatus;

public class ClientHttpServiceException extends BaseHttpException{
    public ClientHttpServiceException(String message, HttpStatus httpStatus, String errorCode) {
        super(message, httpStatus, errorCode);
    }
}
