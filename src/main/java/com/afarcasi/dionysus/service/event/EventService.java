package com.afarcasi.dionysus.service.event;

import com.afarcasi.dionysus.exception.EventNotFoundException;
import com.afarcasi.dionysus.exception.UnauthorizedRoleException;
import com.afarcasi.dionysus.exception.UserNotFoundException;
import com.afarcasi.dionysus.exception.VenueNotFoundException;
import com.afarcasi.dionysus.mapper.EventMapper;
import com.afarcasi.dionysus.model.dto.event.EventCreationDTO;
import com.afarcasi.dionysus.model.dto.event.EventResponseDTO;
import com.afarcasi.dionysus.model.dto.event.EventUpdateDTO;
import com.afarcasi.dionysus.model.entity.event.Event;
import com.afarcasi.dionysus.model.entity.user.User;
import com.afarcasi.dionysus.model.entity.user.UserCategory;
import com.afarcasi.dionysus.model.entity.venue.Venue;
import com.afarcasi.dionysus.repository.event.EventRepository;
import com.afarcasi.dionysus.repository.user.UserRepository;
import com.afarcasi.dionysus.repository.venue.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final VenueRepository venueRepository;
    private final EventMapper eventMapper;

    @Transactional
    public EventResponseDTO createEvent(EventCreationDTO dto) {
        User organizer = userRepository.findById(dto.getOrganizerId())
                .orElseThrow(() -> new UserNotFoundException(dto.getOrganizerId()));

        if (organizer.getRole() != UserCategory.EVENT_ORGANIZER) {
            throw new UnauthorizedRoleException("Only users with EVENT_ORGANIZER role can create events");
        }

        Venue venue = venueRepository.findById(dto.getVenueId())
                .orElseThrow(() -> new VenueNotFoundException(dto.getVenueId()));

        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setCreatedAt(LocalDateTime.now());
        event.setOrganizer(organizer);
        event.setVenue(venue);

        eventRepository.save(event);
        return eventMapper.toResponseDTO(event);
    }

    public Optional<EventResponseDTO> findById(Long id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isPresent()) {
            return Optional.of(eventMapper.toResponseDTO(event.get()));
        }
        return Optional.empty();
    }

    public List<EventResponseDTO> findAll() {
        return eventRepository.findAll().stream()
                .map(eventMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<EventResponseDTO> findByOrganizerId(Long organizerId) {
        return eventRepository.findByOrganizerId(organizerId).stream()
                .map(eventMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<EventResponseDTO> findByVenueId(Long venueId) {
        return eventRepository.findByVenueId(venueId).stream()
                .map(eventMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<EventResponseDTO> findByTitleContaining(String title) {
        return eventRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(eventMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventResponseDTO updateEvent(Long id, EventUpdateDTO dto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));

        eventMapper.updateEntity(dto, event);

        if (dto.getVenueId() != null) {
            Venue venue = venueRepository.findById(dto.getVenueId())
                    .orElseThrow(() -> new VenueNotFoundException(dto.getVenueId()));
            event.setVenue(venue);
        }

        eventRepository.save(event);
        return eventMapper.toResponseDTO(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new EventNotFoundException(id);
        }
        eventRepository.deleteById(id);
    }
}
