package com.test08.domain.order.dto;

import java.util.Map;

public record OrderForm(
        String email,
        String address,
        String postCode,
        Map<Integer, Integer> items
) {
}
