package io.malicki.ticketify.domain.ticket;

import io.malicki.ticketify.common.kafka.TopicNames;
import io.malicki.ticketify.common.kafka.model.DltMessage;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TicketProducer {

    private final KafkaTemplate<String, TicketEvent> ticketEventKafkaTemplate;
    private final KafkaTemplate<String, DltMessage> ticketDltEventKafkaTemplate;

    public void sendTicketEventToTicketTopic(TicketEvent ticketEvent) {
        String key = ticketEvent.ticketId();
        ticketEventKafkaTemplate.send(TopicNames.TICKET, key, ticketEvent);
    }

    public void sendTicketEventToNotificationTopic(TicketEvent ticketEvent) {
        String key = ticketEvent.ticketId();
        ticketEventKafkaTemplate.send(TopicNames.NOTIFICATION, key, ticketEvent);
    }

    // DLT
    public void sendErrorMessageToTicketDltTopic(DltMessage errorMessage) {
        String key = errorMessage.originalKey();
        ticketDltEventKafkaTemplate.send(TopicNames.DLT.TICKET_DLT, key, errorMessage);
    }
}
