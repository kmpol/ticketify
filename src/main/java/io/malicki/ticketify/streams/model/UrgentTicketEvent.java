package io.malicki.ticketify.streams.model;

import io.malicki.ticketify.domain.ticket.TicketEvent;

import java.time.LocalDateTime;
import java.util.List;

public record UrgentTicketEvent(
        String ticketId,
        String visitorId,
        String title,
        String description,
        List<String> matchedKeywords,
        String urgencyLevel,  // "CRITICAL", "HIGH", "MEDIUM"
        LocalDateTime detectedAt
) {
    
    public static final List<String> CRITICAL_KEYWORDS = List.of(
            "critical", "emergency", "outage", "down", "broken"
    );
    
    public static final List<String> HIGH_KEYWORDS = List.of(
            "urgent", "asap", "immediately", "blocker", "production"
    );
    
    public static final List<String> MEDIUM_KEYWORDS = List.of(
            "important", "priority", "soon", "needed"
    );
    
    public static UrgentTicketEvent from(
            TicketEvent ticketEvent, 
            List<String> matchedKeywords, 
            String urgencyLevel
    ) {
        return new UrgentTicketEvent(
                ticketEvent.ticketId(),
                ticketEvent.visitorId(),
                ticketEvent.title(),
                ticketEvent.description(),
                matchedKeywords,
                urgencyLevel,
                LocalDateTime.now()
        );
    }
}
