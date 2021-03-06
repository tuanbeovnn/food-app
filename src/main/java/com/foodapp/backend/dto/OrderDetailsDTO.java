package com.foodapp.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailsDTO {
    private double unitPrice;
    private int quantity;
    private double discount;
    private double total;
}
