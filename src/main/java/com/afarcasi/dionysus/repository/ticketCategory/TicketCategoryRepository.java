package com.afarcasi.dionysus.repository.ticketCategory;

import com.afarcasi.dionysus.model.entity.ticketCategory.TicketCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketCategoryRepository extends JpaRepository<TicketCategory, Long> {
    List<TicketCategory> findByEventId(Long eventId);
}