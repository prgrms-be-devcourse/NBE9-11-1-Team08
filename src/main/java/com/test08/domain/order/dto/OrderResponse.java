package com.test08.domain.order.dto;

import com.test08.domain.order.entity.Order;
import com.test08.domain.orderitem.dto.OrderItemResponse;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String email,
        String address,
        String postcode,
        LocalDateTime orderedAt,
        List<OrderItemResponse> items
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getEmail(),
                order.getAddress(),
                order.getPostcode(),
                order.getOrderedAt(),
                order.getOrderItems().stream().map(OrderItemResponse::from).toList()
        );
    }
}
