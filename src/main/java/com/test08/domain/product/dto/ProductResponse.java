package com.test08.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponse{
    private int productId;
    private String name;
    private int price;
    private String imageUrl;
    private String category;
}