package com.foodapp.backend.service;


import com.foodapp.backend.dto.OrderDTO;
import com.foodapp.backend.dto.response.OrderBuilderResponse;
import com.foodapp.backend.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(OrderDTO orderDTO);

    OrderBuilderResponse findOrderById(Long id);
}
