package com.afarcasi.dionysus.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for updating user password.
 */
@Data
public class UserPasswordUpdateDTO {

    @NotBlank(message = "Password cannot be blank")
    private String newPassword;
}
