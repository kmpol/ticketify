package io.malicki.ticketify.domain.ticket;

import io.malicki.ticketify.common.TopicNames;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TicketConsumer {

    private final TicketProducer ticketProducer;

    public TicketConsumer(TicketProducer ticketProducer) {
        this.ticketProducer = ticketProducer;
    }

    @KafkaListener(
            topics = TopicNames.TICKET,
            groupId = TopicNames.TICKET + "-processor-group"
    )
    public void consume(ConsumerRecord<String, TicketEvent> record, Acknowledgment ack) {
        log.info("⚙️Processing ticket {} from partition {}, offset {}", record.value().ticketId(), record.partition(), record.offset());
        ack.acknowledge();
        ticketProducer.sendTicketEventToNotificationTopic(record.value());
    }
}