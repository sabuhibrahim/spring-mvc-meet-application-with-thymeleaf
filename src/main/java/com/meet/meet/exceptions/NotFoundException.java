package com.meet.meet.exceptions;

public class NotFoundException extends RuntimeException {
    private static final long serialVerisionUID = 138787645387653L;

    public NotFoundException(String message) {
        super(message);
    }
}