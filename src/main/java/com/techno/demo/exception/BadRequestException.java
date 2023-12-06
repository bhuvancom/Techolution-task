package com.techno.demo.exception;

import lombok.Data;

@Data
public class BadRequestException extends RuntimeException {
    private String message;

    public BadRequestException(String msg) {
        super(msg);
        setMessage(msg);
    }
}
