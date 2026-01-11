package com.afarcasi.dionysus.mapper;

import com.afarcasi.dionysus.model.dto.user.UserCreateDTO;
import com.afarcasi.dionysus.model.dto.user.UserResponseDTO;
import com.afarcasi.dionysus.model.dto.user.UserUpdateDTO;
import com.afarcasi.dionysus.model.entity.user.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

/**
 * Handles the mapping of a user to DTOs and vice versa.
 */
@Component
public class UserMapper {

    /**
     * Used to create a User from the user creation DTO.
     */
    public User fromCreateDTO(@NotNull UserCreateDTO dto) {

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPasswordHash(dto.getPassword());
        user.setRole(dto.getRole());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());

        return user;
    }

    /**
     * Used when returning user data to the client
     */
    public UserResponseDTO toResponseDTO(@NotNull User user) {

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());
        return dto;
    }

    /**
     * Used when updating an existing user
     * Only non-null fields will be applied
     */
    public void updateEntity(@NotNull UserUpdateDTO dto, User user) {
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }
    }
}
