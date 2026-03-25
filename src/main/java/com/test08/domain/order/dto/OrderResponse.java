package com.test08.domain.order.dto;

import com.test08.domain.order.entity.Order;
import com.test08.domain.order.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long orderId,
        String email,
        String address,
        String postCode,
        int totalPrice,
        OrderStatus status,
        List<OrderItemDetail> items,
        LocalDateTime updatedTime
) {
    public record OrderItemDetail(
            String name,
            int quantity,
            int price
    ) {
    }

    // Order 엔티티 → DTO 변환
    public static OrderResponse from(Order order) {

        List<OrderItemDetail> itemDetails = order.getOrderItems().stream()
                .map(item -> new OrderItemDetail(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .toList();

        return new OrderResponse(
                order.getOrderId(),
                order.getEmail(),
                order.getAddress(),
                order.getPostCode(),
                order.getTotalPrice(),
                order.getStatus(),
                itemDetails,
                order.getUpdatedTime()
        );
    }
}