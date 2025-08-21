package com.ecommerce.product.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String category;
    private String imageUri;
    private boolean active = true;
}
