package io.malicki.ticketify.kafka;

import io.malicki.ticketify.common.kafka.TopicNames;
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

    // DLTs
    @Bean
    public NewTopic ticketEventsTopicDlt() {
        return TopicBuilder.name(TopicNames.DLT.TICKET_DLT)
                .partitions(1)
                .replicas(1)
                .build();
    }

    //Streams
    @Bean
    public NewTopic spamUsersTopic() {
        return TopicBuilder.name(TopicNames.Streams.SPAM_USERS)
                .partitions(3)
                .replicas(3)
                .build();
    }

    @Bean
    public NewTopic urgentTicketsTopic() {
        return TopicBuilder.name(TopicNames.Streams.URGENT_TICKETS)
                .partitions(3)
                .replicas(3)
                .build();
    }
}