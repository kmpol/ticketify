package io.malicki.ticketify.streams;

import io.malicki.ticketify.common.kafka.TopicNames;
import io.malicki.ticketify.domain.ticket.TicketEvent;
import io.malicki.ticketify.domain.ticket.TicketStatus;
import io.malicki.ticketify.streams.model.UrgentTicketEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UrgentTicketStreamProcessor {

    private final JsonSerde<TicketEvent> ticketEventSerde;

    @Autowired
    public void buildPipeline(StreamsBuilder streamsBuilder) {
        log.info("🚀 Building Urgent Ticket Detector topology...");
        
        JsonSerde<UrgentTicketEvent> urgentTicketEventSerde = new JsonSerde<>(UrgentTicketEvent.class);

        streamsBuilder
                .stream(TopicNames.TICKET, Consumed.with(Serdes.String(), ticketEventSerde))
                .filter((key, value) -> TicketStatus.CREATED.equals(value.status()))
                .filter((k, v) -> containsUrgentKeywords(v))
                .map((k, v) -> KeyValue.pair(k,
                        new UrgentTicketEvent(
                        v.ticketId(),
                        v.visitorId(),
                        v.title(),
                        v.description(),
                        findMatchedKeywords(v),
                        determineUrgencyLevel(v),
                        LocalDateTime.now()))
                )
                .to(TopicNames.Streams.URGENT_TICKETS);
        
        log.info("✅ Urgent Ticket Detector topology built successfully");
        log.info("   - Source: {}", TopicNames.TICKET);
        log.info("   - Sink: {}", TopicNames.Streams.URGENT_TICKETS);
    }

    private boolean containsUrgentKeywords(TicketEvent event) {
        if (event == null) return false;
        
        String textToCheck = (
                (event.title() != null ? event.title() : "") + " " +
                (event.description() != null ? event.description() : "")
        ).toLowerCase();
        
        return UrgentTicketEvent.CRITICAL_KEYWORDS.stream().anyMatch(textToCheck::contains) ||
               UrgentTicketEvent.HIGH_KEYWORDS.stream().anyMatch(textToCheck::contains) ||
               UrgentTicketEvent.MEDIUM_KEYWORDS.stream().anyMatch(textToCheck::contains);
    }

    private List<String> findMatchedKeywords(TicketEvent event) {
        List<String> matched = new ArrayList<>();
        
        String textToCheck = (
                (event.title() != null ? event.title() : "") + " " +
                (event.description() != null ? event.description() : "")
        ).toLowerCase();
        
        for (String keyword : UrgentTicketEvent.CRITICAL_KEYWORDS) {
            if (textToCheck.contains(keyword)) matched.add(keyword);
        }
        for (String keyword : UrgentTicketEvent.HIGH_KEYWORDS) {
            if (textToCheck.contains(keyword)) matched.add(keyword);
        }
        for (String keyword : UrgentTicketEvent.MEDIUM_KEYWORDS) {
            if (textToCheck.contains(keyword)) matched.add(keyword);
        }
        
        return matched;
    }
    
    private String determineUrgencyLevel(TicketEvent event) {
        String textToCheck = (
                (event.title() != null ? event.title() : "") + " " +
                (event.description() != null ? event.description() : "")
        ).toLowerCase();
        
        if (UrgentTicketEvent.CRITICAL_KEYWORDS.stream().anyMatch(textToCheck::contains)) {
            return "CRITICAL";
        }
        if (UrgentTicketEvent.HIGH_KEYWORDS.stream().anyMatch(textToCheck::contains)) {
            return "HIGH";
        }
        if (UrgentTicketEvent.MEDIUM_KEYWORDS.stream().anyMatch(textToCheck::contains)) {
            return "MEDIUM";
        }
        
        return "UNKNOWN";
    }
}
