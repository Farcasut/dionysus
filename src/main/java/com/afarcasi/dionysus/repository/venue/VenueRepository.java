package com.afarcasi.dionysus.repository.venue;

import com.afarcasi.dionysus.model.entity.venue.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findByOwnerId(Long ownerId);
    List<Venue> findByNameContainingIgnoreCase(String venueName);
}