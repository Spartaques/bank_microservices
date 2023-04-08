package com.andriibashuk.userauthservice.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@ToString
@Getter
@Setter
public abstract class BaseHttpException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final String errorCode;

    public BaseHttpException(String message, HttpStatus httpStatus, String errorCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}
