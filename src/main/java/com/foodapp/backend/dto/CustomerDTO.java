package com.foodapp.backend.dto;

import com.foodapp.backend.dto.response.CustomerUserResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO extends AbstractDTO {
    private String address;
    private String city;
    private String phone;
    private CustomerUserResponse user;
}
