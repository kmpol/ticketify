package io.malicki.ticketify.streams;

import io.malicki.ticketify.common.kafka.TopicNames;
import io.malicki.ticketify.streams.model.SpamUserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpamUserConsumer {

    @KafkaListener(
            topics = TopicNames.Streams.SPAM_USERS,
            groupId = "spam-user-handler",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleSpamUser(SpamUserEvent event, Acknowledgment ack) {
        log.warn("═══════════════════════════════════════════════════════");
        log.warn("🚫 SPAM USER DETECTED AND PROCESSED!");
        log.warn("   Visitor ID:    {}", event.visitorId());
        log.warn("   Ticket Count:  {}", event.ticketCount());
        log.warn("   Detected At:   {}", event.detectedAt());
        log.warn("   Reason:        {}", event.reason());
        log.warn("═══════════════════════════════════════════════════════");
        
        // userService.blockUser(event.visitorId());
        
        // alertService.sendAlert("Spam user detected", event);
        
        // spamUserRepository.save(SpamUser.from(event));
        
        ack.acknowledge();
        log.info("✅ Spam user event processed and acknowledged");
    }
}
