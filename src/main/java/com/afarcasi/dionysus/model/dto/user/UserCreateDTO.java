package com.afarcasi.dionysus.model.dto.user;

import com.afarcasi.dionysus.model.entity.user.UserCategory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO handling the user creation.
 */
@Data
public class UserCreateDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private UserCategory role;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}