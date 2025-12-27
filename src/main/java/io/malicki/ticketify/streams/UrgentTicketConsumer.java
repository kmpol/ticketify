package io.malicki.ticketify.streams;

import io.malicki.ticketify.common.kafka.TopicNames;
import io.malicki.ticketify.streams.model.UrgentTicketEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UrgentTicketConsumer {

    @KafkaListener(
            topics = TopicNames.Streams.URGENT_TICKETS,
            groupId = "urgent-ticket-handler",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleUrgentTicket(UrgentTicketEvent event, Acknowledgment ack) {
        String urgencyEmoji = switch (event.urgencyLevel()) {
            case "CRITICAL" -> "🔴";
            case "HIGH" -> "🟠";
            case "MEDIUM" -> "🟡";
            default -> "⚪";
        };
        
        log.warn("═══════════════════════════════════════════════════════");
        log.warn("{} URGENT TICKET DETECTED! Level: {}", urgencyEmoji, event.urgencyLevel());
        log.warn("   Ticket ID:     {}", event.ticketId());
        log.warn("   Visitor ID:    {}", event.visitorId());
        log.warn("   Title:         {}", event.title());
        log.warn("   Keywords:      {}", event.matchedKeywords());
        log.warn("   Detected At:   {}", event.detectedAt());
        log.warn("═══════════════════════════════════════════════════════");
        
        // ticketService.escalate(event.ticketId(), event.urgencyLevel());
        
        // slackService.sendAlert("#urgent-tickets", event);
        
        // slaService.setUrgentSla(event.ticketId(), event.urgencyLevel());
        
        ack.acknowledge();
        log.info("✅ Urgent ticket event processed and acknowledged");
    }
}
