package com.foodapp.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private String productName;
    private String imgHighlight;
    private String image;
    private double price;
    private String size;
    private String description;
    private String nutrition;
    private int quantity;
    private String status;
    private Long categoryId;
}
