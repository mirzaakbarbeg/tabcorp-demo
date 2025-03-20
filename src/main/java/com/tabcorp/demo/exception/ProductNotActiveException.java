package com.tabcorp.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductNotActiveException extends RuntimeException {
    public ProductNotActiveException(String message) {
        super(message);
    }
}
