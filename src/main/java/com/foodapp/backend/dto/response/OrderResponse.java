package com.foodapp.backend.dto.response;


import com.foodapp.backend.dto.OrderDetailsDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String phone;
    private List<OrderDetailsDTO> orderDetails;
}
