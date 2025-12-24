package io.malicki.ticketify.exception.nonretryable;

import io.malicki.ticketify.exception.NonRetryableException;

public class InvalidTicketDataException extends NonRetryableException {
    public InvalidTicketDataException(String message) {
        super(message);
    }
}