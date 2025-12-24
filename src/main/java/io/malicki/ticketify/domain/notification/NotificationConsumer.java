package io.malicki.ticketify.domain.notification;

import io.malicki.ticketify.common.TopicNames;
import io.malicki.ticketify.domain.ticket.TicketEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = TopicNames.NOTIFICATION,
            groupId = "ticket-notifications-group"
    )
    public void consume(ConsumerRecord<String, TicketEvent> record, Acknowledgment ack) {
        notificationService.processTicketNotification(record, ack);
    }
}
