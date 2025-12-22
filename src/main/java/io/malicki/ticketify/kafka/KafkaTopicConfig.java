package io.malicki.ticketify.kafka;

import io.malicki.ticketify.common.TopicNames;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic ticketEventsTopic() {
        return TopicBuilder.name(TopicNames.TICKET)
                .partitions(3)
                .replicas(3)
                .build();
    }

    @Bean
    public NewTopic notificationEventsTopic() {
        return TopicBuilder.name(TopicNames.NOTIFICATION)
                .partitions(3)
                .replicas(3)
                .build();
    }
}