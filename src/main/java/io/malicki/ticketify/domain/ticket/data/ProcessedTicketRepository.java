package io.malicki.ticketify.domain.ticket.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessedTicketRepository extends JpaRepository<ProcessedTicket, Long> {

    Optional<ProcessedTicket> findByTicketId(String ticketId);
}
