package com.test08.domain.orderitem.dto;

public record OrderItemForm(
        Long productId,
        int quantity
) {
}
