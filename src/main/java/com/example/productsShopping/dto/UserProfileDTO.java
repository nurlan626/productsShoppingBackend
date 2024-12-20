package com.example.productsShopping.dto;

import lombok.Data;

@Data
public class UserProfileDTO {
    private String name;
    private String surname;
    private String email;
    private String username;

    public UserProfileDTO(String name, String surname, String email, String username) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.username = username;
    }
}
