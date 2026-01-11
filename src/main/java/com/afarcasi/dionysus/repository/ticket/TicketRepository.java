package com.afarcasi.dionysus.repository.ticket;

import com.afarcasi.dionysus.model.entity.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByOrderId(Long orderId);
}