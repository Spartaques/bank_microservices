package com.andriibashuk.applicationservice.exception;

import org.springframework.http.HttpStatus;

public class StateMachineEventNotAcceptedException extends BaseHttpException {
    public StateMachineEventNotAcceptedException(String message, HttpStatus httpStatus, String errorCode) {
        super(message, httpStatus, errorCode);
    }
}
