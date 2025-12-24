package io.malicki.ticketify.exception.retryable;

import io.malicki.ticketify.exception.RetryableException;

public class ExternalServiceUnavailableException extends RetryableException {
    public ExternalServiceUnavailableException(String message) {
        super(message);
    }
}
