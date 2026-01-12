package com.afarcasi.dionysus.mapper;

import com.afarcasi.dionysus.model.dto.event.EventResponseDTO;
import com.afarcasi.dionysus.model.dto.event.EventUpdateDTO;
import com.afarcasi.dionysus.model.entity.event.Event;
import com.afarcasi.dionysus.model.entity.ticketCategory.TicketCategory;
import com.afarcasi.dionysus.model.entity.user.User;
import com.afarcasi.dionysus.model.entity.user.UserCategory;
import com.afarcasi.dionysus.model.entity.venue.Venue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventMapperTest {

    private EventMapper eventMapper;

    private Event event;
    private EventUpdateDTO eventUpdateDTO;
    private User organizer;
    private Venue venue;
    private TicketCategory ticketCategory;

    @BeforeEach
    void setUp() {
        eventMapper = new EventMapper();

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

        ticketCategory = new TicketCategory();
        ticketCategory.setId(1L);
        ticketCategory.setName("VIP");
        ticketCategory.setPrice(100.0);
        ticketCategory.setAvailableSpots(50);

        event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");
        event.setDescription("Test Description");
        event.setCreatedAt(LocalDateTime.now());
        event.setEventDate(LocalDateTime.now().plusDays(30));
        event.setOrganizer(organizer);
        event.setVenue(venue);
        event.setCategories(Arrays.asList(ticketCategory));

        eventUpdateDTO = new EventUpdateDTO();
        eventUpdateDTO.setTitle("Updated Event");
        eventUpdateDTO.setDescription("Updated Description");
        eventUpdateDTO.setEventDate(LocalDateTime.now().plusDays(60));
    }

    @Test
    void toResponseDTO_ShouldMapAllFieldsCorrectly() {
        EventResponseDTO result = eventMapper.toResponseDTO(event);

        assertNotNull(result);
        assertEquals(event.getId(), result.getId());
        assertEquals(event.getTitle(), result.getTitle());
        assertEquals(event.getDescription(), result.getDescription());
        assertEquals(event.getCreatedAt(), result.getCreatedAt());
        assertEquals(event.getEventDate(), result.getEventDate());
        assertNotNull(result.getOrganizer());
        assertEquals(organizer.getId(), result.getOrganizer().getId());
        assertEquals(organizer.getEmail(), result.getOrganizer().getEmail());
        assertEquals(organizer.getUsername(), result.getOrganizer().getUsername());
        assertNotNull(result.getVenue());
        assertEquals(venue.getId(), result.getVenue().getId());
        assertEquals(venue.getName(), result.getVenue().getName());
        assertEquals(venue.getCapacity(), result.getVenue().getCapacity());
        assertNotNull(result.getTicketCategories());
        assertEquals(1, result.getTicketCategories().size());
        assertEquals(ticketCategory.getId(), result.getTicketCategories().get(0).getId());
        assertEquals(ticketCategory.getName(), result.getTicketCategories().get(0).getName());
    }

    @Test
    void toResponseDTO_WhenOrganizerIsNull_ShouldNotSetOrganizer() {
        event.setOrganizer(null);

        EventResponseDTO result = eventMapper.toResponseDTO(event);

        assertNotNull(result);
        assertNull(result.getOrganizer());
    }

    @Test
    void toResponseDTO_WhenVenueIsNull_ShouldNotSetVenue() {
        event.setVenue(null);

        EventResponseDTO result = eventMapper.toResponseDTO(event);

        assertNotNull(result);
        assertNull(result.getVenue());
    }

    @Test
    void toResponseDTO_WhenCategoriesIsNull_ShouldNotSetCategories() {
        event.setCategories(null);

        EventResponseDTO result = eventMapper.toResponseDTO(event);

        assertNotNull(result);
        assertNull(result.getTicketCategories());
    }

    @Test
    void toResponseDTO_WhenCategoriesIsEmpty_ShouldSetEmptyList() {
        event.setCategories(Arrays.asList());

        EventResponseDTO result = eventMapper.toResponseDTO(event);

        assertNotNull(result);
        assertNotNull(result.getTicketCategories());
        assertEquals(0, result.getTicketCategories().size());
    }

    @Test
    void updateEntity_WhenAllFieldsAreProvided_ShouldUpdateAllFields() {
        Event eventToUpdate = new Event();
        eventToUpdate.setTitle("Old Title");
        eventToUpdate.setDescription("Old Description");
        eventToUpdate.setEventDate(LocalDateTime.now());

        eventMapper.updateEntity(eventUpdateDTO, eventToUpdate);

        assertEquals(eventUpdateDTO.getTitle(), eventToUpdate.getTitle());
        assertEquals(eventUpdateDTO.getDescription(), eventToUpdate.getDescription());
        assertEquals(eventUpdateDTO.getEventDate(), eventToUpdate.getEventDate());
    }

    @Test
    void updateEntity_WhenTitleIsNull_ShouldNotUpdateTitle() {
        Event eventToUpdate = new Event();
        eventToUpdate.setTitle("Old Title");
        EventUpdateDTO dto = new EventUpdateDTO();
        dto.setTitle(null);
        dto.setDescription("New Description");
        dto.setEventDate(LocalDateTime.now().plusDays(60));

        eventMapper.updateEntity(dto, eventToUpdate);

        assertEquals("Old Title", eventToUpdate.getTitle());
        assertEquals("New Description", eventToUpdate.getDescription());
        assertEquals(dto.getEventDate(), eventToUpdate.getEventDate());
    }

    @Test
    void updateEntity_WhenDescriptionIsNull_ShouldNotUpdateDescription() {
        Event eventToUpdate = new Event();
        eventToUpdate.setDescription("Old Description");
        EventUpdateDTO dto = new EventUpdateDTO();
        dto.setTitle("New Title");
        dto.setDescription(null);
        dto.setEventDate(LocalDateTime.now().plusDays(60));

        eventMapper.updateEntity(dto, eventToUpdate);

        assertEquals("New Title", eventToUpdate.getTitle());
        assertEquals("Old Description", eventToUpdate.getDescription());
        assertEquals(dto.getEventDate(), eventToUpdate.getEventDate());
    }

    @Test
    void updateEntity_WhenEventDateIsNull_ShouldNotUpdateEventDate() {
        LocalDateTime oldDate = LocalDateTime.now();
        Event eventToUpdate = new Event();
        eventToUpdate.setEventDate(oldDate);
        EventUpdateDTO dto = new EventUpdateDTO();
        dto.setTitle("New Title");
        dto.setDescription("New Description");
        dto.setEventDate(null);

        eventMapper.updateEntity(dto, eventToUpdate);

        assertEquals("New Title", eventToUpdate.getTitle());
        assertEquals("New Description", eventToUpdate.getDescription());
        assertEquals(oldDate, eventToUpdate.getEventDate());
    }

    @Test
    void updateEntity_WhenOnlyTitleIsProvided_ShouldUpdateOnlyTitle() {
        Event eventToUpdate = new Event();
        eventToUpdate.setTitle("Old Title");
        eventToUpdate.setDescription("Old Description");
        eventToUpdate.setEventDate(LocalDateTime.now());
        EventUpdateDTO dto = new EventUpdateDTO();
        dto.setTitle("New Title");
        dto.setDescription(null);
        dto.setEventDate(null);

        eventMapper.updateEntity(dto, eventToUpdate);

        assertEquals("New Title", eventToUpdate.getTitle());
        assertEquals("Old Description", eventToUpdate.getDescription());
        assertNotNull(eventToUpdate.getEventDate());
    }
}
