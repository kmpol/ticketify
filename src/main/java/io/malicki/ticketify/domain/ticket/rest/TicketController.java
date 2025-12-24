package io.malicki.ticketify.domain.ticket.rest;

import io.malicki.ticketify.domain.ticket.TicketEvent;
import io.malicki.ticketify.domain.ticket.TicketProducer;
import io.malicki.ticketify.exception.nonretryable.InvalidTicketDataException;
import jakarta.annotation.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class TicketController {

    private final TicketProducer ticketProducer;

    public TicketController(TicketProducer ticketProducer) {
        this.ticketProducer = ticketProducer;
    }


    @PostMapping("/tickets")
    public ResponseEntity<?> create(@RequestBody CreateTicketRequest request, @Nullable @RequestParam String ticketId) {
        TicketEvent ticketEvent;
        if(ticketId == null) {
            ticketEvent = TicketEvent.from(request);
        } else {
            ticketEvent = TicketEvent.from(request, ticketId);
        }
        if (ticketId != null && ticketId.equals("2137")) {
            throw new InvalidTicketDataException("2137 big no no!");
        }
        ticketProducer.sendTicketEventToTicketTopic(ticketEvent);
        return ResponseEntity.ok(ticketEvent);
    }
}
