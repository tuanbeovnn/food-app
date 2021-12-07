package com.foodapp.backend.service;


import com.foodapp.backend.dto.CustomerDTO;
import com.foodapp.backend.dto.request.CustomerRequest;
import com.foodapp.backend.dto.response.ProfileResponse;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerRequest customerRequest);
    ProfileResponse userProfile();

}
