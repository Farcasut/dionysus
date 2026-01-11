package com.afarcasi.dionysus.model.dto.user;

import com.afarcasi.dionysus.model.entity.user.UserCategory;
import lombok.Data;

/**
 * DTO handling the general response that a user will receive.
 */
@Data
public class UserResponseDTO {
    private Long id;
    private String email;
    private String username;
    private UserCategory role;
    private String firstName;
    private String lastName;
}