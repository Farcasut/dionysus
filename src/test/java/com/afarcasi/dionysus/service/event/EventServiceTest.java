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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VenueRepository venueRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventService eventService;

    private Event event;
    private EventResponseDTO eventResponseDTO;
    private EventCreationDTO eventCreationDTO;
    private EventUpdateDTO eventUpdateDTO;
    private User organizer;
    private Venue venue;

    @BeforeEach
    void setUp() {
        organizer = new User();
        organizer.setId(1L);
        organizer.setEmail("organizer@example.com");
        organizer.setUsername("organizer");
        organizer.setRole(UserCategory.EVENT_ORGANIZER);
        organizer.setFirstName("Organizer");
        organizer.setLastName("User");

        venue = new Venue();
        venue.setId(1L);
        venue.setName("Test Venue");
        venue.setCapacity(100);

        event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");
        event.setDescription("Test Description");
        event.setEventDate(LocalDateTime.now().plusDays(30));
        event.setCreatedAt(LocalDateTime.now());
        event.setOrganizer(organizer);
        event.setVenue(venue);

        eventResponseDTO = new EventResponseDTO();
        eventResponseDTO.setId(1L);
        eventResponseDTO.setTitle("Test Event");
        eventResponseDTO.setDescription("Test Description");
        eventResponseDTO.setEventDate(event.getEventDate());
        eventResponseDTO.setCreatedAt(event.getCreatedAt());

        eventCreationDTO = new EventCreationDTO();
        eventCreationDTO.setTitle("New Event");
        eventCreationDTO.setDescription("New Description");
        eventCreationDTO.setEventDate(LocalDateTime.now().plusDays(30));
        eventCreationDTO.setOrganizerId(1L);
        eventCreationDTO.setVenueId(1L);

        eventUpdateDTO = new EventUpdateDTO();
        eventUpdateDTO.setTitle("Updated Event");
        eventUpdateDTO.setDescription("Updated Description");
    }

    @Test
    void createEvent_WhenOrganizerIsEventOrganizer_ShouldReturnEventResponseDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(organizer));
        when(venueRepository.findById(1L)).thenReturn(Optional.of(venue));
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(eventMapper.toResponseDTO(any(Event.class))).thenReturn(eventResponseDTO);

        EventResponseDTO result = eventService.createEvent(eventCreationDTO);

        assertNotNull(result);
        assertEquals(eventResponseDTO.getId(), result.getId());
        verify(userRepository).findById(1L);
        verify(venueRepository).findById(1L);
        verify(eventRepository).save(any(Event.class));
        verify(eventMapper).toResponseDTO(any(Event.class));
    }

    @Test
    void createEvent_WhenOrganizerDoesNotExist_ShouldThrowUserNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> eventService.createEvent(eventCreationDTO));
        verify(userRepository).findById(1L);
        verify(venueRepository, never()).findById(anyLong());
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void createEvent_WhenOrganizerIsNotEventOrganizer_ShouldThrowUnauthorizedRoleException() {
        organizer.setRole(UserCategory.NORMAL_USER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(organizer));

        assertThrows(UnauthorizedRoleException.class, () -> eventService.createEvent(eventCreationDTO));
        verify(userRepository).findById(1L);
        verify(venueRepository, never()).findById(anyLong());
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void createEvent_WhenVenueDoesNotExist_ShouldThrowVenueNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(organizer));
        when(venueRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(VenueNotFoundException.class, () -> eventService.createEvent(eventCreationDTO));
        verify(userRepository).findById(1L);
        verify(venueRepository).findById(1L);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void findById_WhenEventExists_ShouldReturnEventResponseDTO() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventMapper.toResponseDTO(event)).thenReturn(eventResponseDTO);

        Optional<EventResponseDTO> result = eventService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(eventResponseDTO.getId(), result.get().getId());
        verify(eventRepository).findById(1L);
        verify(eventMapper).toResponseDTO(event);
    }

    @Test
    void findById_WhenEventDoesNotExist_ShouldReturnEmpty() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<EventResponseDTO> result = eventService.findById(1L);

        assertTrue(result.isEmpty());
        verify(eventRepository).findById(1L);
        verify(eventMapper, never()).toResponseDTO(any(Event.class));
    }

    @Test
    void findAll_ShouldReturnListOfEventResponseDTOs() {
        Event event2 = new Event();
        event2.setId(2L);
        List<Event> events = Arrays.asList(event, event2);
        EventResponseDTO eventResponseDTO2 = new EventResponseDTO();
        eventResponseDTO2.setId(2L);
        when(eventRepository.findAll()).thenReturn(events);
        when(eventMapper.toResponseDTO(event)).thenReturn(eventResponseDTO);
        when(eventMapper.toResponseDTO(event2)).thenReturn(eventResponseDTO2);

        List<EventResponseDTO> result = eventService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(eventRepository).findAll();
        verify(eventMapper, times(2)).toResponseDTO(any(Event.class));
    }

    @Test
    void findByOrganizerId_ShouldReturnListOfEventResponseDTOs() {
        List<Event> events = Arrays.asList(event);
        when(eventRepository.findByOrganizerId(1L)).thenReturn(events);
        when(eventMapper.toResponseDTO(event)).thenReturn(eventResponseDTO);

        List<EventResponseDTO> result = eventService.findByOrganizerId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(eventRepository).findByOrganizerId(1L);
        verify(eventMapper).toResponseDTO(event);
    }

    @Test
    void findByVenueId_ShouldReturnListOfEventResponseDTOs() {
        List<Event> events = Arrays.asList(event);
        when(eventRepository.findByVenueId(1L)).thenReturn(events);
        when(eventMapper.toResponseDTO(event)).thenReturn(eventResponseDTO);

        List<EventResponseDTO> result = eventService.findByVenueId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(eventRepository).findByVenueId(1L);
        verify(eventMapper).toResponseDTO(event);
    }

    @Test
    void findByTitleContaining_ShouldReturnListOfEventResponseDTOs() {
        List<Event> events = Arrays.asList(event);
        when(eventRepository.findByTitleContainingIgnoreCase("Test")).thenReturn(events);
        when(eventMapper.toResponseDTO(event)).thenReturn(eventResponseDTO);

        List<EventResponseDTO> result = eventService.findByTitleContaining("Test");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(eventRepository).findByTitleContainingIgnoreCase("Test");
        verify(eventMapper).toResponseDTO(event);
    }

    @Test
    void updateEvent_WhenEventExists_ShouldReturnUpdatedEventResponseDTO() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.toResponseDTO(event)).thenReturn(eventResponseDTO);
        doNothing().when(eventMapper).updateEntity(eventUpdateDTO, event);

        EventResponseDTO result = eventService.updateEvent(1L, eventUpdateDTO);

        assertNotNull(result);
        verify(eventRepository).findById(1L);
        verify(eventMapper).updateEntity(eventUpdateDTO, event);
        verify(eventRepository).save(event);
        verify(eventMapper).toResponseDTO(event);
    }

    @Test
    void updateEvent_WhenEventDoesNotExist_ShouldThrowEventNotFoundException() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.updateEvent(1L, eventUpdateDTO));
        verify(eventRepository).findById(1L);
        verify(eventMapper, never()).updateEntity(any(), any());
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void updateEvent_WhenVenueIdIsProvided_ShouldUpdateVenue() {
        Venue newVenue = new Venue();
        newVenue.setId(2L);
        newVenue.setName("New Venue");
        eventUpdateDTO.setVenueId(2L);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(venueRepository.findById(2L)).thenReturn(Optional.of(newVenue));
        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.toResponseDTO(event)).thenReturn(eventResponseDTO);
        doNothing().when(eventMapper).updateEntity(eventUpdateDTO, event);

        EventResponseDTO result = eventService.updateEvent(1L, eventUpdateDTO);

        assertNotNull(result);
        assertEquals(newVenue, event.getVenue());
        verify(eventRepository).findById(1L);
        verify(venueRepository).findById(2L);
        verify(eventMapper).updateEntity(eventUpdateDTO, event);
        verify(eventRepository).save(event);
    }

    @Test
    void updateEvent_WhenVenueDoesNotExist_ShouldThrowVenueNotFoundException() {
        eventUpdateDTO.setVenueId(2L);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(venueRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(VenueNotFoundException.class, () -> eventService.updateEvent(1L, eventUpdateDTO));
        verify(eventRepository).findById(1L);
        verify(venueRepository).findById(2L);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void deleteEvent_WhenEventExists_ShouldDeleteEvent() {
        when(eventRepository.existsById(1L)).thenReturn(true);
        doNothing().when(eventRepository).deleteById(1L);

        eventService.deleteEvent(1L);

        verify(eventRepository).existsById(1L);
        verify(eventRepository).deleteById(1L);
    }

    @Test
    void deleteEvent_WhenEventDoesNotExist_ShouldThrowEventNotFoundException() {
        when(eventRepository.existsById(1L)).thenReturn(false);

        assertThrows(EventNotFoundException.class, () -> eventService.deleteEvent(1L));
        verify(eventRepository).existsById(1L);
        verify(eventRepository, never()).deleteById(anyLong());
    }
}
