package io.malicki.ticketify.domain.ticket;

import io.malicki.ticketify.domain.ticket.data.ProcessedTicketEntity;
import io.malicki.ticketify.domain.ticket.data.ProcessedTicketRepository;
import io.malicki.ticketify.exception.retryable.ExternalServiceUnavailableException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TicketService {

    private final ProcessedTicketRepository processedTicketRepository;
    private final TicketProducer ticketProducer;

    @Transactional
    public void processTicketCreation(TicketEvent ticketEvent) {
        String ticketId = ticketEvent.ticketId();

        if (isTicketProcessed(ticketId)) {
            return;
        }

        log.info("⚙️Processing ticket of id: {}", ticketId);
        if(true) {
            throw new ExternalServiceUnavailableException("Testing retry");
        }

        ticketProducer.sendTicketEventToNotificationTopic(ticketEvent);
        markTicketCreationProcessed(ticketId);
    }

    private boolean isTicketProcessed(String ticketId) {
        Optional<ProcessedTicketEntity> processedTicketOptional = processedTicketRepository.findByTicketId(ticketId);
        if(processedTicketOptional.isPresent()) {
            log.warn("❌Ticket already has been processed of given ticketId: {}, skipping", ticketId);
            return true;
        }
        return false;
    }

    private void markTicketCreationProcessed(String ticketId) {
        processedTicketRepository.save(ProcessedTicketEntity.builder().ticketId(ticketId).build());
    }
}
