package io.malicki.ticketify.domain.ticket;

import io.malicki.ticketify.common.TopicNames;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TicketProducer {

    private final KafkaTemplate<String, TicketEvent> ticketEventKafkaTemplate;

    public void sendTicketEventToTicketTopic(TicketEvent ticketEvent) {
        String key = ticketEvent.ticketId();
        ticketEventKafkaTemplate.send(TopicNames.TICKET, key, ticketEvent);
    }

    public void sendTicketEventToNotificationTopic(TicketEvent ticketEvent) {
        String key = ticketEvent.ticketId();
        ticketEventKafkaTemplate.send(TopicNames.NOTIFICATION, key, ticketEvent);
    }
}
