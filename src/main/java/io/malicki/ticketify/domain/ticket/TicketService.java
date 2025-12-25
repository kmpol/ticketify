package io.malicki.ticketify.domain.ticket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.malicki.ticketify.common.kafka.TopicNames;
import io.malicki.ticketify.common.outbox.OutboxStatus;
import io.malicki.ticketify.common.outbox.data.OutboxEvent;
import io.malicki.ticketify.common.outbox.data.OutboxEventRepository;
import io.malicki.ticketify.domain.ticket.data.ProcessedTicket;
import io.malicki.ticketify.domain.ticket.data.ProcessedTicketRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TicketService {

    private final ProcessedTicketRepository processedTicketRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final TicketProducer ticketProducer;
    private final ObjectMapper objectMapper;

    @Transactional
    public void processTicketCreation(TicketEvent ticketEvent) {
        String ticketId = ticketEvent.ticketId();

        if (isTicketProcessed(ticketId)) {
            return;
        }

        log.info("⚙️Processing ticket of id: {}", ticketId);
        process(ticketEvent);
        log.info("⚙️Ticket of id: {} processed, ticket event sent to notification to be handled", ticketId);
    }

    @Transactional
    public void createTicket(TicketEvent ticketEvent) throws JsonProcessingException {
        outboxEventRepository.save(
                OutboxEvent.builder()
                        .aggregateType("TICKET")
                        .aggregateId(ticketEvent.ticketId())
                        .topic(TopicNames.TICKET)
                        .payload(objectMapper.writeValueAsString(ticketEvent))
                        .status(OutboxStatus.CREATED)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    private boolean isTicketProcessed(String ticketId) {
        Optional<ProcessedTicket> processedTicketOptional = processedTicketRepository.findByTicketId(ticketId);
        if(processedTicketOptional.isPresent()) {
            log.warn("❌Ticket already has been processed of given ticketId: {}, skipping", ticketId);
            return true;
        }
        return false;
    }

    private void process(TicketEvent ticketEvent) {
        String ticketId = ticketEvent.ticketId();
        ticketProducer.sendTicketEventToNotificationTopic(ticketEvent);
        processedTicketRepository.save(ProcessedTicket.builder().ticketId(ticketId).build());
    }
}
