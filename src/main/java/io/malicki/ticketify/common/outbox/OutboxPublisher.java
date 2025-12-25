package io.malicki.ticketify.common.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.malicki.ticketify.common.kafka.model.DltMessage;
import io.malicki.ticketify.common.outbox.data.OutboxEvent;
import io.malicki.ticketify.common.outbox.data.OutboxEventRepository;
import io.malicki.ticketify.domain.ticket.TicketEvent;
import io.malicki.ticketify.domain.ticket.TicketProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class OutboxPublisher {

    private final OutboxEventRepository outboxRepository;
    private final TicketProducer ticketProducer;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void publish() {
        List<OutboxEvent> pendingEvents = outboxRepository.getByStatus(OutboxStatus.CREATED);
        log.info("Found events of count: {}", pendingEvents.size());

        for (OutboxEvent event: pendingEvents) {
            log.info("Processing in publisher the event: {}", event);
            //TODO: needs refactor, type safety and nesting problem
            if (event.getAggregateType().equals("TICKET")) {
                publishTicketToKafka(event);
            }
            outboxRepository.save(event);
        }
        log.info("All events has been sent to topics!");
    }

    private void publishTicketToKafka(OutboxEvent event) {
        try {
            TicketEvent ticketEvent = objectMapper.readValue(event.getPayload(), TicketEvent.class);
            ticketProducer.sendTicketEventToTicketTopic(ticketEvent);
            event.setStatus(OutboxStatus.COMPLETED);
            event.setSentAt(LocalDateTime.now());
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize event {}: {}", event.getId(), e.getMessage());
            event.setStatus(OutboxStatus.ERROR);

            ticketProducer.sendErrorMessageToTicketDltTopic(
                    new DltMessage(
                            event.getTopic(),
                            event.getAggregateId(),
                            event.getPayload(),
                            e.getMessage(),
                            e.getClass().getName(),
                            0,
                            LocalDateTime.now()
                    )
            );
        }
    }
}
