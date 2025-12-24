package io.malicki.ticketify.domain.ticket;

import io.malicki.ticketify.common.TopicNames;
import io.malicki.ticketify.exception.NonRetryableException;
import io.malicki.ticketify.exception.RetryableException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class TicketConsumer {

    private final TicketService ticketService;
    private final int MAX_RETRIES = 3;

    @KafkaListener(
            topics = TopicNames.TICKET,
            groupId = TopicNames.TICKET + "-processor-group"
    )
    public void consume(ConsumerRecord<String, TicketEvent> record, Acknowledgment ack) throws InterruptedException {
        processWithRetry(record.value(), 1);
        ack.acknowledge();
    }

    private void processWithRetry(TicketEvent ticketEvent, int retryCount) throws InterruptedException {
        String ticketId = ticketEvent.ticketId();

        try {
            ticketService.processTicketCreation(ticketEvent);
        } catch (RetryableException retryableException) {
            if (retryCount <= MAX_RETRIES) {
                log.info("Retry attempt {}/3 for ticketId: {}", retryCount, ticketId);
                Thread.sleep(calculateSleep(retryCount));
                processWithRetry(ticketEvent, retryCount + 1);
            } else {
                log.warn("Max retries! Sending ticket of id: {} to DLT!", ticketId);
            }
        } catch (NonRetryableException nonRetryableException) {
            log.warn("Non-Retryable exception! Sending ticket of id: {} to DLT!", ticketId);
        }
    }

    private long calculateSleep(int retryCount) {
        return (long) Math.pow(2, retryCount) * 1000L;
    }
}