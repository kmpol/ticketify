package io.malicki.ticketify.domain.notification.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessedNotificationRepository extends JpaRepository<ProcessedNotificationEntity, Long> {
    Optional<ProcessedNotificationEntity> getProcessedNotificationEntityByTicketId(String ticketId);
}
