package com.example.gateway.sftp.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class InvalidRequestData extends RuntimeException {

    public InvalidRequestData(Class<?> clazz) {

        super(clazz.getSimpleName() + " is invalid");
    }
}
