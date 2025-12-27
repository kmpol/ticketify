package io.malicki.ticketify.domain.ticket.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.malicki.ticketify.domain.ticket.TicketEvent;
import io.malicki.ticketify.domain.ticket.TicketService;
import io.malicki.ticketify.exception.nonretryable.InvalidTicketDataException;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/tickets")
    public ResponseEntity<?> create(@RequestBody CreateTicketRequest request, @Nullable @RequestParam String ticketId) throws JsonProcessingException {
        TicketEvent ticketEvent;
        if(ticketId == null) {
            ticketEvent = TicketEvent.from(request);
        } else {
            ticketEvent = TicketEvent.from(request, ticketId);
        }
        if (ticketId != null && ticketId.equals("2137")) {
            throw new InvalidTicketDataException("2137 big no no!");
        }
        ticketService.createTicket(ticketEvent);
        return ResponseEntity.ok(ticketEvent);
    }
}
