package com.afarcasi.dionysus.service.venue;

import com.afarcasi.dionysus.exception.UnauthorizedRoleException;
import com.afarcasi.dionysus.exception.UserNotFoundException;
import com.afarcasi.dionysus.exception.VenueNotFoundException;
import com.afarcasi.dionysus.mapper.VenueMapper;
import com.afarcasi.dionysus.model.dto.venue.VenueCreationDTO;
import com.afarcasi.dionysus.model.dto.venue.VenueUpdateDTO;
import com.afarcasi.dionysus.model.dto.venue.VenueViewDTO;
import com.afarcasi.dionysus.model.entity.user.User;
import com.afarcasi.dionysus.model.entity.user.UserCategory;
import com.afarcasi.dionysus.model.entity.venue.Venue;
import com.afarcasi.dionysus.repository.user.UserRepository;
import com.afarcasi.dionysus.repository.venue.VenueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenueServiceTest {

    @Mock
    private VenueRepository venueRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VenueMapper venueMapper;

    @InjectMocks
    private VenueService venueService;

    private Venue venue;
    private VenueViewDTO venueViewDTO;
    private VenueCreationDTO venueCreationDTO;
    private VenueUpdateDTO venueUpdateDTO;
    private User owner;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1L);
        owner.setEmail("owner@example.com");
        owner.setUsername("venueowner");
        owner.setRole(UserCategory.VENUE_OWNER);
        owner.setFirstName("Venue");
        owner.setLastName("Owner");

        venue = new Venue();
        venue.setId(1L);
        venue.setName("Test Venue");
        venue.setCapacity(100);
        venue.setOwner(owner);

        venueViewDTO = new VenueViewDTO();
        venueViewDTO.setId(1L);
        venueViewDTO.setName("Test Venue");
        venueViewDTO.setCapacity(100);

        venueCreationDTO = new VenueCreationDTO();
        venueCreationDTO.setName("New Venue");
        venueCreationDTO.setCapacity(200);
        venueCreationDTO.setOwnerId(1L);

        venueUpdateDTO = new VenueUpdateDTO();
        venueUpdateDTO.setName("Updated Venue");
        venueUpdateDTO.setCapacity(150);
    }

    @Test
    void createVenue_WhenOwnerIsVenueOwner_ShouldReturnVenueViewDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(venueRepository.save(any(Venue.class))).thenReturn(venue);
        when(venueMapper.toViewDTO(any(Venue.class))).thenReturn(venueViewDTO);

        VenueViewDTO result = venueService.createVenue(venueCreationDTO);

        assertNotNull(result);
        assertEquals(venueViewDTO.getId(), result.getId());
        verify(userRepository).findById(1L);
        verify(venueRepository).save(any(Venue.class));
        verify(venueMapper).toViewDTO(any(Venue.class));
    }

    @Test
    void createVenue_WhenOwnerDoesNotExist_ShouldThrowUserNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> venueService.createVenue(venueCreationDTO));
        verify(userRepository).findById(1L);
        verify(venueRepository, never()).save(any(Venue.class));
    }

    @Test
    void createVenue_WhenOwnerIsNotVenueOwner_ShouldThrowUnauthorizedRoleException() {
        owner.setRole(UserCategory.NORMAL_USER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));

        assertThrows(UnauthorizedRoleException.class, () -> venueService.createVenue(venueCreationDTO));
        verify(userRepository).findById(1L);
        verify(venueRepository, never()).save(any(Venue.class));
    }

    @Test
    void findById_WhenVenueExists_ShouldReturnVenueViewDTO() {
        when(venueRepository.findById(1L)).thenReturn(Optional.of(venue));
        when(venueMapper.toViewDTO(venue)).thenReturn(venueViewDTO);

        Optional<VenueViewDTO> result = venueService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(venueViewDTO.getId(), result.get().getId());
        verify(venueRepository).findById(1L);
        verify(venueMapper).toViewDTO(venue);
    }

    @Test
    void findById_WhenVenueDoesNotExist_ShouldReturnEmpty() {
        when(venueRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<VenueViewDTO> result = venueService.findById(1L);

        assertTrue(result.isEmpty());
        verify(venueRepository).findById(1L);
        verify(venueMapper, never()).toViewDTO(any(Venue.class));
    }

    @Test
    void findAll_ShouldReturnListOfVenueViewDTOs() {
        Venue venue2 = new Venue();
        venue2.setId(2L);
        List<Venue> venues = Arrays.asList(venue, venue2);
        VenueViewDTO venueViewDTO2 = new VenueViewDTO();
        venueViewDTO2.setId(2L);
        when(venueRepository.findAll()).thenReturn(venues);
        when(venueMapper.toViewDTO(venue)).thenReturn(venueViewDTO);
        when(venueMapper.toViewDTO(venue2)).thenReturn(venueViewDTO2);

        List<VenueViewDTO> result = venueService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(venueRepository).findAll();
        verify(venueMapper, times(2)).toViewDTO(any(Venue.class));
    }

    @Test
    void findByOwnerId_ShouldReturnListOfVenueViewDTOs() {
        List<Venue> venues = Arrays.asList(venue);
        when(venueRepository.findByOwnerId(1L)).thenReturn(venues);
        when(venueMapper.toViewDTO(venue)).thenReturn(venueViewDTO);

        List<VenueViewDTO> result = venueService.findByOwnerId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(venueRepository).findByOwnerId(1L);
        verify(venueMapper).toViewDTO(venue);
    }

    @Test
    void findByNameContaining_ShouldReturnListOfVenueViewDTOs() {
        List<Venue> venues = Arrays.asList(venue);
        when(venueRepository.findByNameContainingIgnoreCase("Test")).thenReturn(venues);
        when(venueMapper.toViewDTO(venue)).thenReturn(venueViewDTO);

        List<VenueViewDTO> result = venueService.findByNameContaining("Test");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(venueRepository).findByNameContainingIgnoreCase("Test");
        verify(venueMapper).toViewDTO(venue);
    }

    @Test
    void updateVenue_WhenVenueExists_ShouldReturnUpdatedVenueViewDTO() {
        when(venueRepository.findById(1L)).thenReturn(Optional.of(venue));
        when(venueRepository.save(venue)).thenReturn(venue);
        when(venueMapper.toViewDTO(venue)).thenReturn(venueViewDTO);
        doNothing().when(venueMapper).updateEntity(venueUpdateDTO, venue);

        VenueViewDTO result = venueService.updateVenue(1L, venueUpdateDTO);

        assertNotNull(result);
        verify(venueRepository).findById(1L);
        verify(venueMapper).updateEntity(venueUpdateDTO, venue);
        verify(venueRepository).save(venue);
        verify(venueMapper).toViewDTO(venue);
    }

    @Test
    void updateVenue_WhenVenueDoesNotExist_ShouldThrowVenueNotFoundException() {
        when(venueRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(VenueNotFoundException.class, () -> venueService.updateVenue(1L, venueUpdateDTO));
        verify(venueRepository).findById(1L);
        verify(venueMapper, never()).updateEntity(any(), any());
        verify(venueRepository, never()).save(any(Venue.class));
    }

    @Test
    void deleteVenue_WhenVenueExists_ShouldDeleteVenue() {
        when(venueRepository.existsById(1L)).thenReturn(true);
        doNothing().when(venueRepository).deleteById(1L);

        venueService.deleteVenue(1L);

        verify(venueRepository).existsById(1L);
        verify(venueRepository).deleteById(1L);
    }

    @Test
    void deleteVenue_WhenVenueDoesNotExist_ShouldThrowVenueNotFoundException() {
        when(venueRepository.existsById(1L)).thenReturn(false);

        assertThrows(VenueNotFoundException.class, () -> venueService.deleteVenue(1L));
        verify(venueRepository).existsById(1L);
        verify(venueRepository, never()).deleteById(anyLong());
    }
}
