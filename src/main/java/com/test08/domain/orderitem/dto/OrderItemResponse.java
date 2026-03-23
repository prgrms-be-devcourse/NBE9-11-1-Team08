package com.test08.domain.orderitem.dto;

import java.time.LocalDateTime;

public record OrderItemResponse(
        Long orderItemId,
        Long orderId,
        Long productId,
        Integer quantity,
        Integer orderPriceItem,
        LocalDateTime orderTime
) {
}
