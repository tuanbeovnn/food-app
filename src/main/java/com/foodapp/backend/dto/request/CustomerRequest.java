package com.foodapp.backend.dto.request;

import com.foodapp.backend.dto.CustomerUserDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CustomerRequest {
    @NotEmpty(message = "Address is required")
    private String address;
    private String city;
    @NotEmpty(message = "Phone is required")
    private String phone;

    private CustomerUserDTO user;
}
