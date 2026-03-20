package com.test08.domain.orderitem.dto;

import com.test08.domain.orderitem.entity.OrderItem;

public record OrderItemResponse(
        Long id,
        Long productId,
        String productName,
        int quantity
) {
    public static OrderItemResponse from(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getProduct().getId(),
                orderItem.getProduct().getName(),
                orderItem.getQuantity()
        );
    }
}
