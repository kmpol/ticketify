package io.malicki.ticketify.streams;

import io.malicki.ticketify.domain.ticket.TicketEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.HashMap;
import java.util.Map;

/**
 * Config Kafka Streams.
 * 
 * @EnableKafkaStreams - StreamsBuilder is enabled
 */
@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsConfiguration() {
        Map<String, Object> props = new HashMap<>();
        
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "ticketify-streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class);
        
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.AT_LEAST_ONCE);
        
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1000);
        
        props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams");
        
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 1);
        
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public JsonSerde<TicketEvent> ticketEventSerde() {
        JsonSerde<TicketEvent> serde = new JsonSerde<>(TicketEvent.class);
        serde.configure(
                Map.of(JsonDeserializer.TRUSTED_PACKAGES, "*"),
                false  // isKey = false
        );
        return serde;
    }
}
