package io.malicki.ticketify.exception;

public abstract class NonRetryableException extends RuntimeException {
    public NonRetryableException(String message) {
        super(message);
    }
}
