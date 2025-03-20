package com.tabcorp.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TransactionCostExceededException extends RuntimeException {
    public TransactionCostExceededException(String message) {
        super(message);
    }
}

