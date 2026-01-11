package com.afarcasi.dionysus.repository.event;

import com.afarcasi.dionysus.model.entity.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByTitleContainingIgnoreCase (String title);
    List<Event> findByOrganizerId(Long organizerId);
    List<Event> findByVenueId(Long venueId);


}
