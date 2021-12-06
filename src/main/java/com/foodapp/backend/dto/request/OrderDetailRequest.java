package com.foodapp.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailRequest {
    private Long productId;
    private Integer quantity;
}
