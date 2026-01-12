package com.afarcasi.dionysus.mapper;

import com.afarcasi.dionysus.model.dto.user.UserCreateDTO;
import com.afarcasi.dionysus.model.dto.user.UserResponseDTO;
import com.afarcasi.dionysus.model.dto.user.UserUpdateDTO;
import com.afarcasi.dionysus.model.entity.user.User;
import com.afarcasi.dionysus.model.entity.user.UserCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    private User user;
    private UserCreateDTO userCreateDTO;
    private UserUpdateDTO userUpdateDTO;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPasswordHash("password123");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(UserCategory.NORMAL_USER);

        userCreateDTO = new UserCreateDTO();
        userCreateDTO.setEmail("new@example.com");
        userCreateDTO.setUsername("newuser");
        userCreateDTO.setPassword("newpassword");
        userCreateDTO.setFirstName("Jane");
        userCreateDTO.setLastName("Smith");
        userCreateDTO.setRole(UserCategory.EVENT_ORGANIZER);

        userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setEmail("updated@example.com");
        userUpdateDTO.setFirstName("Updated");
        userUpdateDTO.setLastName("Name");
    }

    @Test
    void fromCreateDTO_ShouldMapAllFieldsCorrectly() {
        User result = userMapper.fromCreateDTO(userCreateDTO);

        assertNotNull(result);
        assertEquals(userCreateDTO.getEmail(), result.getEmail());
        assertEquals(userCreateDTO.getUsername(), result.getUsername());
        assertEquals(userCreateDTO.getPassword(), result.getPasswordHash());
        assertEquals(userCreateDTO.getFirstName(), result.getFirstName());
        assertEquals(userCreateDTO.getLastName(), result.getLastName());
        assertEquals(userCreateDTO.getRole(), result.getRole());
        assertNull(result.getId()); // ID should not be set in creation
    }

    @Test
    void toResponseDTO_ShouldMapAllFieldsCorrectly() {
        UserResponseDTO result = userMapper.toResponseDTO(user);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getRole(), result.getRole());
    }

    @Test
    void updateEntity_WhenAllFieldsAreProvided_ShouldUpdateAllFields() {
        User userToUpdate = new User();
        userToUpdate.setId(1L);
        userToUpdate.setEmail("old@example.com");
        userToUpdate.setFirstName("Old");
        userToUpdate.setLastName("Name");

        userMapper.updateEntity(userUpdateDTO, userToUpdate);

        assertEquals(userUpdateDTO.getEmail(), userToUpdate.getEmail());
        assertEquals(userUpdateDTO.getFirstName(), userToUpdate.getFirstName());
        assertEquals(userUpdateDTO.getLastName(), userToUpdate.getLastName());
        assertEquals(1L, userToUpdate.getId()); // ID should remain unchanged
    }

    @Test
    void updateEntity_WhenEmailIsNull_ShouldNotUpdateEmail() {
        User userToUpdate = new User();
        userToUpdate.setEmail("old@example.com");
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setEmail(null);
        dto.setFirstName("New");
        dto.setLastName("Name");

        userMapper.updateEntity(dto, userToUpdate);

        assertEquals("old@example.com", userToUpdate.getEmail());
        assertEquals("New", userToUpdate.getFirstName());
        assertEquals("Name", userToUpdate.getLastName());
    }

    @Test
    void updateEntity_WhenFirstNameIsNull_ShouldNotUpdateFirstName() {
        User userToUpdate = new User();
        userToUpdate.setFirstName("Old");
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setEmail("new@example.com");
        dto.setFirstName(null);
        dto.setLastName("Name");

        userMapper.updateEntity(dto, userToUpdate);

        assertEquals("new@example.com", userToUpdate.getEmail());
        assertEquals("Old", userToUpdate.getFirstName());
        assertEquals("Name", userToUpdate.getLastName());
    }

    @Test
    void updateEntity_WhenLastNameIsNull_ShouldNotUpdateLastName() {
        User userToUpdate = new User();
        userToUpdate.setLastName("Old");
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setEmail("new@example.com");
        dto.setFirstName("New");
        dto.setLastName(null);

        userMapper.updateEntity(dto, userToUpdate);

        assertEquals("new@example.com", userToUpdate.getEmail());
        assertEquals("New", userToUpdate.getFirstName());
        assertEquals("Old", userToUpdate.getLastName());
    }

    @Test
    void updateEntity_WhenOnlyEmailIsProvided_ShouldUpdateOnlyEmail() {
        User userToUpdate = new User();
        userToUpdate.setEmail("old@example.com");
        userToUpdate.setFirstName("Old");
        userToUpdate.setLastName("Name");
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setEmail("new@example.com");
        dto.setFirstName(null);
        dto.setLastName(null);

        userMapper.updateEntity(dto, userToUpdate);

        assertEquals("new@example.com", userToUpdate.getEmail());
        assertEquals("Old", userToUpdate.getFirstName());
        assertEquals("Name", userToUpdate.getLastName());
    }
}
