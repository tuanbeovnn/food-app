package com.foodapp.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerUserResponse {
    private String email;
    private String username;
    private String fullName;
}
