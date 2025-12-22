package io.malicki.ticketify.domain.ticket.rest;

import io.malicki.ticketify.domain.ticket.TicketEvent;
import io.malicki.ticketify.domain.ticket.TicketProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class TicketController {

    private final TicketProducer ticketProducer;

    public TicketController(TicketProducer ticketProducer) {
        this.ticketProducer = ticketProducer;
    }


    @PostMapping("/tickets")
    public ResponseEntity<?> create(@RequestBody CreateTicketRequest request) {
        TicketEvent ticketEvent = TicketEvent.from(request);
        ticketProducer.sendTicketEventToTicketTopic(ticketEvent);
        return ResponseEntity.ok(ticketEvent);
    }
}
