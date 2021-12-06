package com.foodapp.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerUserDTO {
    private String email;
    private String username;
    private String password;
    private String fullName;
}
