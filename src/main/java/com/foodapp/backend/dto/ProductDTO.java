package com.foodapp.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO extends AbstractDTO {
    private String productName;
    private String imgHighlight;
    private String image;
}
