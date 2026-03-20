package com.test08.domain.product.dto;

import com.test08.domain.product.entity.Product;

public record ProductResponse(
        Long id,
        String category,
        String name,
        int price,
        String imageUrl
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getCategory(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );
    }
}
