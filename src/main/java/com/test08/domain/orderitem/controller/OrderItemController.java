package com.test08.domain.orderitem.controller;

import com.test08.domain.orderitem.dto.OrderItemResponse;
import com.test08.domain.orderitem.service.OrderItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping
    public List<OrderItemResponse> getOrderItems() {
        return orderItemService.getOrderItems();
    }
}
