package com.afarcasi.dionysus.mapper;

import com.afarcasi.dionysus.model.dto.ticketCategory.TicketCategoryUpdateDTO;
import com.afarcasi.dionysus.model.dto.ticketCategory.TicketCategoryViewDTO;
import com.afarcasi.dionysus.model.entity.ticketCategory.TicketCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketCategoryMapperTest {

    private TicketCategoryMapper ticketCategoryMapper;

    private TicketCategory ticketCategory;
    private TicketCategoryUpdateDTO ticketCategoryUpdateDTO;

    @BeforeEach
    void setUp() {
        ticketCategoryMapper = new TicketCategoryMapper();

        ticketCategory = new TicketCategory();
        ticketCategory.setId(1L);
        ticketCategory.setName("VIP");
        ticketCategory.setPrice(100.0);
        ticketCategory.setAvailableSpots(50);

        ticketCategoryUpdateDTO = new TicketCategoryUpdateDTO();
        ticketCategoryUpdateDTO.setName("Premium");
        ticketCategoryUpdateDTO.setPrice(150.0);
        ticketCategoryUpdateDTO.setAvailableSpots(75);
    }

    @Test
    void toViewDTO_ShouldMapAllFieldsCorrectly() {
        TicketCategoryViewDTO result = ticketCategoryMapper.toViewDTO(ticketCategory);

        assertNotNull(result);
        assertEquals(ticketCategory.getId(), result.getId());
        assertEquals(ticketCategory.getName(), result.getName());
        assertEquals(ticketCategory.getPrice(), result.getPrice());
        assertEquals(ticketCategory.getAvailableSpots(), result.getAvailableSpots());
    }

    @Test
    void updateEntity_WhenAllFieldsAreProvided_ShouldUpdateAllFields() {
        TicketCategory categoryToUpdate = new TicketCategory();
        categoryToUpdate.setName("Old");
        categoryToUpdate.setPrice(50.0);
        categoryToUpdate.setAvailableSpots(25);

        ticketCategoryMapper.updateEntity(ticketCategoryUpdateDTO, categoryToUpdate);

        assertEquals(ticketCategoryUpdateDTO.getName(), categoryToUpdate.getName());
        assertEquals(ticketCategoryUpdateDTO.getPrice(), categoryToUpdate.getPrice());
        assertEquals(ticketCategoryUpdateDTO.getAvailableSpots(), categoryToUpdate.getAvailableSpots());
    }

    @Test
    void updateEntity_WhenNameIsNull_ShouldNotUpdateName() {
        TicketCategory categoryToUpdate = new TicketCategory();
        categoryToUpdate.setName("Old");
        TicketCategoryUpdateDTO dto = new TicketCategoryUpdateDTO();
        dto.setName(null);
        dto.setPrice(150.0);
        dto.setAvailableSpots(75);

        ticketCategoryMapper.updateEntity(dto, categoryToUpdate);

        assertEquals("Old", categoryToUpdate.getName());
        assertEquals(150.0, categoryToUpdate.getPrice());
        assertEquals(75, categoryToUpdate.getAvailableSpots());
    }

    @Test
    void updateEntity_WhenPriceIsNull_ShouldNotUpdatePrice() {
        TicketCategory categoryToUpdate = new TicketCategory();
        categoryToUpdate.setPrice(50.0);
        TicketCategoryUpdateDTO dto = new TicketCategoryUpdateDTO();
        dto.setName("Premium");
        dto.setPrice(null);
        dto.setAvailableSpots(75);

        ticketCategoryMapper.updateEntity(dto, categoryToUpdate);

        assertEquals("Premium", categoryToUpdate.getName());
        assertEquals(50.0, categoryToUpdate.getPrice());
        assertEquals(75, categoryToUpdate.getAvailableSpots());
    }

    @Test
    void updateEntity_WhenAvailableSpotsIsNull_ShouldNotUpdateAvailableSpots() {
        TicketCategory categoryToUpdate = new TicketCategory();
        categoryToUpdate.setAvailableSpots(25);
        TicketCategoryUpdateDTO dto = new TicketCategoryUpdateDTO();
        dto.setName("Premium");
        dto.setPrice(150.0);
        dto.setAvailableSpots(null);

        ticketCategoryMapper.updateEntity(dto, categoryToUpdate);

        assertEquals("Premium", categoryToUpdate.getName());
        assertEquals(150.0, categoryToUpdate.getPrice());
        assertEquals(25, categoryToUpdate.getAvailableSpots());
    }

    @Test
    void updateEntity_WhenOnlyNameIsProvided_ShouldUpdateOnlyName() {
        TicketCategory categoryToUpdate = new TicketCategory();
        categoryToUpdate.setName("Old");
        categoryToUpdate.setPrice(50.0);
        categoryToUpdate.setAvailableSpots(25);
        TicketCategoryUpdateDTO dto = new TicketCategoryUpdateDTO();
        dto.setName("Premium");
        dto.setPrice(null);
        dto.setAvailableSpots(null);

        ticketCategoryMapper.updateEntity(dto, categoryToUpdate);

        assertEquals("Premium", categoryToUpdate.getName());
        assertEquals(50.0, categoryToUpdate.getPrice());
        assertEquals(25, categoryToUpdate.getAvailableSpots());
    }
}
