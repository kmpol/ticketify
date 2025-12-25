package io.malicki.ticketify.common.outbox.data;

import io.malicki.ticketify.common.outbox.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    @Query("select oe from OutboxEvent oe where oe.status = ?1")
    List<OutboxEvent> getByStatus(OutboxStatus status);
}
