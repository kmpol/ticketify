package io.malicki.ticketify.domain.ticket.rest;

import java.util.Objects;

public record CreateTicketRequest(String visitorId, String title, String description) {
    public CreateTicketRequest {
        Objects.requireNonNull(visitorId);
        Objects.requireNonNull(title);
        Objects.requireNonNull(description);
    }
}
