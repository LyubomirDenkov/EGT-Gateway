package com.egt.gateway.exceptions;

public class DuplicateRequestException extends RuntimeException {
    public static final String DUPLICATE_REQUEST_MESSAGE = "Duplicate request ID";

    public DuplicateRequestException() {
    }

    public DuplicateRequestException(String message) {
        super(message);
    }
}
