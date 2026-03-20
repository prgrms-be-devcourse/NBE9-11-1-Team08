package com.test08.domain.order.dto;

import com.test08.domain.orderitem.dto.OrderItemForm;
import java.util.List;

public record OrderForm(
        String email,
        String address,
        String postcode,
        List<OrderItemForm> items
) {
}
