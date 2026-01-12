package com.afarcasi.dionysus.service.ticketCategory;

import com.afarcasi.dionysus.exception.EventNotFoundException;
import com.afarcasi.dionysus.exception.TicketCategoryNotFoundException;
import com.afarcasi.dionysus.mapper.TicketCategoryMapper;
import com.afarcasi.dionysus.model.dto.ticketCategory.TicketCategoryCreationDTO;
import com.afarcasi.dionysus.model.dto.ticketCategory.TicketCategoryUpdateDTO;
import com.afarcasi.dionysus.model.dto.ticketCategory.TicketCategoryViewDTO;
import com.afarcasi.dionysus.model.entity.event.Event;
import com.afarcasi.dionysus.model.entity.ticketCategory.TicketCategory;
import com.afarcasi.dionysus.model.entity.user.User;
import com.afarcasi.dionysus.model.entity.user.UserCategory;
import com.afarcasi.dionysus.repository.event.EventRepository;
import com.afarcasi.dionysus.repository.ticketCategory.TicketCategoryRepository;
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
class TicketCategoryServiceTest {

    @Mock
    private TicketCategoryRepository ticketCategoryRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TicketCategoryMapper ticketCategoryMapper;

    @InjectMocks
    private TicketCategoryService ticketCategoryService;

    private TicketCategory ticketCategory;
    private TicketCategoryViewDTO ticketCategoryViewDTO;
    private TicketCategoryCreationDTO ticketCategoryCreationDTO;
    private TicketCategoryUpdateDTO ticketCategoryUpdateDTO;
    private Event event;
    private User organizer;

    @BeforeEach
    void setUp() {
        organizer = new User();
        organizer.setId(1L);
        organizer.setEmail("organizer@example.com");
        organizer.setUsername("organizer");
        organizer.setRole(UserCategory.EVENT_ORGANIZER);
        organizer.setFirstName("Organizer");
        organizer.setLastName("User");

        event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");
        event.setDescription("Test Description");
        event.setEventDate(LocalDateTime.now().plusDays(30));
        event.setOrganizer(organizer);

        ticketCategory = new TicketCategory();
        ticketCategory.setId(1L);
        ticketCategory.setName("VIP");
        ticketCategory.setPrice(100.0);
        ticketCategory.setAvailableSpots(50);
        ticketCategory.setEvent(event);

        ticketCategoryViewDTO = new TicketCategoryViewDTO();
        ticketCategoryViewDTO.setId(1L);
        ticketCategoryViewDTO.setName("VIP");
        ticketCategoryViewDTO.setPrice(100.0);
        ticketCategoryViewDTO.setAvailableSpots(50);

        ticketCategoryCreationDTO = new TicketCategoryCreationDTO();
        ticketCategoryCreationDTO.setName("General");
        ticketCategoryCreationDTO.setPrice(50.0);
        ticketCategoryCreationDTO.setAvailableSpots(100);
        ticketCategoryCreationDTO.setEventId(1L);

        ticketCategoryUpdateDTO = new TicketCategoryUpdateDTO();
        ticketCategoryUpdateDTO.setName("Premium");
        ticketCategoryUpdateDTO.setPrice(150.0);
        ticketCategoryUpdateDTO.setAvailableSpots(75);
    }

    @Test
    void createTicketCategory_WhenEventExists_ShouldReturnTicketCategoryViewDTO() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(ticketCategoryRepository.save(any(TicketCategory.class))).thenReturn(ticketCategory);
        when(ticketCategoryMapper.toViewDTO(any(TicketCategory.class))).thenReturn(ticketCategoryViewDTO);

        TicketCategoryViewDTO result = ticketCategoryService.createTicketCategory(ticketCategoryCreationDTO);

        assertNotNull(result);
        assertEquals(ticketCategoryViewDTO.getId(), result.getId());
        verify(eventRepository).findById(1L);
        verify(ticketCategoryRepository).save(any(TicketCategory.class));
        verify(ticketCategoryMapper).toViewDTO(any(TicketCategory.class));
    }

    @Test
    void createTicketCategory_WhenEventDoesNotExist_ShouldThrowEventNotFoundException() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> ticketCategoryService.createTicketCategory(ticketCategoryCreationDTO));
        verify(eventRepository).findById(1L);
        verify(ticketCategoryRepository, never()).save(any(TicketCategory.class));
    }

    @Test
    void findById_WhenTicketCategoryExists_ShouldReturnTicketCategoryViewDTO() {
        when(ticketCategoryRepository.findById(1L)).thenReturn(Optional.of(ticketCategory));
        when(ticketCategoryMapper.toViewDTO(ticketCategory)).thenReturn(ticketCategoryViewDTO);

        Optional<TicketCategoryViewDTO> result = ticketCategoryService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(ticketCategoryViewDTO.getId(), result.get().getId());
        verify(ticketCategoryRepository).findById(1L);
        verify(ticketCategoryMapper).toViewDTO(ticketCategory);
    }

    @Test
    void findById_WhenTicketCategoryDoesNotExist_ShouldReturnEmpty() {
        when(ticketCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<TicketCategoryViewDTO> result = ticketCategoryService.findById(1L);

        assertTrue(result.isEmpty());
        verify(ticketCategoryRepository).findById(1L);
        verify(ticketCategoryMapper, never()).toViewDTO(any(TicketCategory.class));
    }

    @Test
    void findAll_ShouldReturnListOfTicketCategoryViewDTOs() {
        TicketCategory ticketCategory2 = new TicketCategory();
        ticketCategory2.setId(2L);
        List<TicketCategory> categories = Arrays.asList(ticketCategory, ticketCategory2);
        TicketCategoryViewDTO ticketCategoryViewDTO2 = new TicketCategoryViewDTO();
        ticketCategoryViewDTO2.setId(2L);
        when(ticketCategoryRepository.findAll()).thenReturn(categories);
        when(ticketCategoryMapper.toViewDTO(ticketCategory)).thenReturn(ticketCategoryViewDTO);
        when(ticketCategoryMapper.toViewDTO(ticketCategory2)).thenReturn(ticketCategoryViewDTO2);

        List<TicketCategoryViewDTO> result = ticketCategoryService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ticketCategoryRepository).findAll();
        verify(ticketCategoryMapper, times(2)).toViewDTO(any(TicketCategory.class));
    }

    @Test
    void findByEventId_ShouldReturnListOfTicketCategoryViewDTOs() {
        List<TicketCategory> categories = Arrays.asList(ticketCategory);
        when(ticketCategoryRepository.findByEventId(1L)).thenReturn(categories);
        when(ticketCategoryMapper.toViewDTO(ticketCategory)).thenReturn(ticketCategoryViewDTO);

        List<TicketCategoryViewDTO> result = ticketCategoryService.findByEventId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(ticketCategoryRepository).findByEventId(1L);
        verify(ticketCategoryMapper).toViewDTO(ticketCategory);
    }

    @Test
    void findByOrganizerId_ShouldReturnListOfTicketCategoryViewDTOs() {
        List<Event> events = Arrays.asList(event);
        List<TicketCategory> categories = Arrays.asList(ticketCategory);
        when(eventRepository.findByOrganizerId(1L)).thenReturn(events);
        when(ticketCategoryRepository.findByEventId(1L)).thenReturn(categories);
        when(ticketCategoryMapper.toViewDTO(ticketCategory)).thenReturn(ticketCategoryViewDTO);

        List<TicketCategoryViewDTO> result = ticketCategoryService.findByOrganizerId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(eventRepository).findByOrganizerId(1L);
        verify(ticketCategoryRepository).findByEventId(1L);
        verify(ticketCategoryMapper).toViewDTO(ticketCategory);
    }

    @Test
    void updateTicketCategory_WhenTicketCategoryExists_ShouldReturnUpdatedTicketCategoryViewDTO() {
        when(ticketCategoryRepository.findById(1L)).thenReturn(Optional.of(ticketCategory));
        when(ticketCategoryRepository.save(ticketCategory)).thenReturn(ticketCategory);
        when(ticketCategoryMapper.toViewDTO(ticketCategory)).thenReturn(ticketCategoryViewDTO);
        doNothing().when(ticketCategoryMapper).updateEntity(ticketCategoryUpdateDTO, ticketCategory);

        TicketCategoryViewDTO result = ticketCategoryService.updateTicketCategory(1L, ticketCategoryUpdateDTO);

        assertNotNull(result);
        verify(ticketCategoryRepository).findById(1L);
        verify(ticketCategoryMapper).updateEntity(ticketCategoryUpdateDTO, ticketCategory);
        verify(ticketCategoryRepository).save(ticketCategory);
        verify(ticketCategoryMapper).toViewDTO(ticketCategory);
    }

    @Test
    void updateTicketCategory_WhenTicketCategoryDoesNotExist_ShouldThrowTicketCategoryNotFoundException() {
        when(ticketCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TicketCategoryNotFoundException.class, () -> ticketCategoryService.updateTicketCategory(1L, ticketCategoryUpdateDTO));
        verify(ticketCategoryRepository).findById(1L);
        verify(ticketCategoryMapper, never()).updateEntity(any(), any());
        verify(ticketCategoryRepository, never()).save(any(TicketCategory.class));
    }

    @Test
    void deleteTicketCategory_WhenTicketCategoryExists_ShouldDeleteTicketCategory() {
        when(ticketCategoryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(ticketCategoryRepository).deleteById(1L);

        ticketCategoryService.deleteTicketCategory(1L);

        verify(ticketCategoryRepository).existsById(1L);
        verify(ticketCategoryRepository).deleteById(1L);
    }

    @Test
    void deleteTicketCategory_WhenTicketCategoryDoesNotExist_ShouldThrowTicketCategoryNotFoundException() {
        when(ticketCategoryRepository.existsById(1L)).thenReturn(false);

        assertThrows(TicketCategoryNotFoundException.class, () -> ticketCategoryService.deleteTicketCategory(1L));
        verify(ticketCategoryRepository).existsById(1L);
        verify(ticketCategoryRepository, never()).deleteById(anyLong());
    }
}
