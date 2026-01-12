package com.afarcasi.dionysus.mapper;

import com.afarcasi.dionysus.model.dto.venue.VenueUpdateDTO;
import com.afarcasi.dionysus.model.dto.venue.VenueViewDTO;
import com.afarcasi.dionysus.model.entity.user.User;
import com.afarcasi.dionysus.model.entity.user.UserCategory;
import com.afarcasi.dionysus.model.entity.venue.Venue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VenueMapperTest {

    private VenueMapper venueMapper;

    private Venue venue;
    private VenueUpdateDTO venueUpdateDTO;

    @BeforeEach
    void setUp() {
        venueMapper = new VenueMapper();

        User owner = new User();
        owner.setId(1L);
        owner.setRole(UserCategory.VENUE_OWNER);

        venue = new Venue();
        venue.setId(1L);
        venue.setName("Test Venue");
        venue.setCapacity(100);
        venue.setOwner(owner);

        venueUpdateDTO = new VenueUpdateDTO();
        venueUpdateDTO.setName("Updated Venue");
        venueUpdateDTO.setCapacity(150);
    }

    @Test
    void toViewDTO_ShouldMapAllFieldsCorrectly() {
        VenueViewDTO result = venueMapper.toViewDTO(venue);

        assertNotNull(result);
        assertEquals(venue.getId(), result.getId());
        assertEquals(venue.getName(), result.getName());
        assertEquals(venue.getCapacity(), result.getCapacity());
    }

    @Test
    void updateEntity_WhenAllFieldsAreProvided_ShouldUpdateAllFields() {
        Venue venueToUpdate = new Venue();
        venueToUpdate.setName("Old");
        venueToUpdate.setCapacity(50);

        venueMapper.updateEntity(venueUpdateDTO, venueToUpdate);

        assertEquals(venueUpdateDTO.getName(), venueToUpdate.getName());
        assertEquals(venueUpdateDTO.getCapacity(), venueToUpdate.getCapacity());
    }

    @Test
    void updateEntity_WhenNameIsNull_ShouldNotUpdateName() {
        Venue venueToUpdate = new Venue();
        venueToUpdate.setName("Old");
        VenueUpdateDTO dto = new VenueUpdateDTO();
        dto.setName(null);
        dto.setCapacity(150);

        venueMapper.updateEntity(dto, venueToUpdate);

        assertEquals("Old", venueToUpdate.getName());
        assertEquals(150, venueToUpdate.getCapacity());
    }

    @Test
    void updateEntity_WhenCapacityIsNull_ShouldNotUpdateCapacity() {
        Venue venueToUpdate = new Venue();
        venueToUpdate.setCapacity(50);
        VenueUpdateDTO dto = new VenueUpdateDTO();
        dto.setName("Updated");
        dto.setCapacity(null);

        venueMapper.updateEntity(dto, venueToUpdate);

        assertEquals("Updated", venueToUpdate.getName());
        assertEquals(50, venueToUpdate.getCapacity());
    }

    @Test
    void updateEntity_WhenOnlyNameIsProvided_ShouldUpdateOnlyName() {
        Venue venueToUpdate = new Venue();
        venueToUpdate.setName("Old");
        venueToUpdate.setCapacity(50);
        VenueUpdateDTO dto = new VenueUpdateDTO();
        dto.setName("Updated");
        dto.setCapacity(null);

        venueMapper.updateEntity(dto, venueToUpdate);

        assertEquals("Updated", venueToUpdate.getName());
        assertEquals(50, venueToUpdate.getCapacity());
    }
}
