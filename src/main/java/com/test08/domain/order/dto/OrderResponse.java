package com.test08.domain.order.dto;

import com.test08.domain.order.entity.Order;
import com.test08.domain.order.entity.OrderStatus;

public record OrderResponse(
        Long orderId,
        String email,
        String address,
        String postCode,
        int totalPrice,
        OrderStatus status
) {
    // Order 엔티티 → DTO 변환
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getOrderId(),
                order.getEmail(),
                order.getAddress(),
                order.getPostCode(),
                order.getTotalPrice(),
                order.getStatus()
        );
    }
}
