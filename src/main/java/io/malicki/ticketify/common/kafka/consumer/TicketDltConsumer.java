package io.malicki.ticketify.common.kafka.consumer;

import io.malicki.ticketify.common.kafka.TopicNames;
import io.malicki.ticketify.common.kafka.model.DltMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class TicketDltConsumer {

    @KafkaListener(
            topics = TopicNames.DLT.TICKET_DLT,
            groupId = TopicNames.DLT.TICKET_DLT + "-processor-group"
    )
    public void consume(ConsumerRecord<String, DltMessage> record, Acknowledgment ack) {
        log.info("Data currently processed in the {} topic is: {}", TopicNames.DLT.TICKET_DLT, record.value());
        ack.acknowledge();
    }
}
