package com.techno.demo.exception;

import lombok.Data;

@Data
public class ResourceNotFoundException extends RuntimeException {
    private String message;

    public ResourceNotFoundException(String msg) {
        super(msg);
        setMessage(msg);
    }
}
