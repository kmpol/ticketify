package io.malicki.ticketify.streams;

import io.malicki.ticketify.common.kafka.TopicNames;
import io.malicki.ticketify.domain.ticket.TicketEvent;
import io.malicki.ticketify.domain.ticket.TicketStatus;
import io.malicki.ticketify.streams.model.SpamUserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class AntiSpamStreamProcessor {

    private final JsonSerde<TicketEvent> ticketEventSerde;

    @Autowired
    public void buildPipeline(StreamsBuilder streamsBuilder) {

        streamsBuilder
                .stream(TopicNames.TICKET, Consumed.with(Serdes.String(), ticketEventSerde))
                .filter((key, value) -> TicketStatus.CREATED.equals(value.status()))
                .selectKey((key, value) -> value.visitorId())
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(1)))
                .count()
                .toStream()
                .filter((key, value) -> value >= 3)
                .map((stringWindowed, value) -> KeyValue.pair(stringWindowed.key(), SpamUserEvent.of(stringWindowed.key(), value)))
                .to(TopicNames.Streams.SPAM_USERS);

    }
}
