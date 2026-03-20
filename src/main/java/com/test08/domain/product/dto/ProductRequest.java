package com.test08.domain.product.dto;

public record ProductRequest(
        String category,
        String name,
        int price,
        String imageUrl
) {
}
