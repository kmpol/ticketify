package io.malicki.ticketify.domain.ticket;

import io.malicki.ticketify.domain.ticket.rest.CreateTicketRequest;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketEvent(String ticketId,
                          String visitorId,
                          String title,
                          String description,
                          TicketStatus status,
                          LocalDateTime createdAt
) {
    public static TicketEvent from(CreateTicketRequest createTicketRequest) {
        LocalDateTime now = LocalDateTime.now();
        String ticketId = UUID.randomUUID().toString();

        return new TicketEvent(
                ticketId,
                createTicketRequest.visitorId(),
                createTicketRequest.title(),
                createTicketRequest.description(),
                TicketStatus.CREATED,
                now);
    }

    public static TicketEvent from(CreateTicketRequest createTicketRequest, String ticketId) {
        LocalDateTime now = LocalDateTime.now();

        return new TicketEvent(
                ticketId,
                createTicketRequest.visitorId(),
                createTicketRequest.title(),
                createTicketRequest.description(),
                TicketStatus.CREATED,
                now);
    }
}
