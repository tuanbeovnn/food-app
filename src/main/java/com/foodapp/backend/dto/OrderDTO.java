package com.foodapp.backend.dto;

import com.foodapp.backend.dto.request.OrderDetailRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDTO extends AbstractDTO{
    private String name;
    private String address;
    private String city;
    private String phone;
    private List<OrderDetailRequest> orderDetailRequests;
}
