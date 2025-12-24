package io.malicki.ticketify.exception;

public abstract class RetryableException extends RuntimeException {
    public RetryableException(String message) {
        super(message);
    }
}

