package io.malicki.ticketify.domain.notification;

import io.malicki.ticketify.domain.notification.data.ProcessedNotificationEntity;
import io.malicki.ticketify.domain.notification.data.ProcessedNotificationRepository;
import io.malicki.ticketify.domain.ticket.TicketEvent;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class NotificationService {

    private final ProcessedNotificationRepository processedNotificationRepository;

    @Transactional
    public void processTicketNotification(ConsumerRecord<String, TicketEvent> record, Acknowledgment ack) {
        String ticketId = record.value().ticketId();
        if (processedNotificationRepository.getProcessedNotificationEntityByTicketId(ticketId).isPresent()) {
            log.warn("❌Notification Ticket already has been processed of given ticketId: {}, skipping", ticketId);
            ack.acknowledge();
            return;
        }
        log.info("\uD83D\uDCE7 Sending notification for ticket: {} to visitor: {}", record.value().ticketId(), record.value().visitorId());
        markNotificationTicketAsProcessed(ticketId);
        ack.acknowledge();
    }

    private void markNotificationTicketAsProcessed(String ticketId) {
        processedNotificationRepository.save(ProcessedNotificationEntity.builder().ticketId(ticketId).build());
    }
}
