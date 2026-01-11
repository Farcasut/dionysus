package com.afarcasi.dionysus.model.dto.user;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String username;
    private String password;
}