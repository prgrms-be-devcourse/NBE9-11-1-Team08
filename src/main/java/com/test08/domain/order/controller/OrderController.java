package com.test08.domain.order.controller;

import com.test08.domain.order.dto.OrderForm;
import com.test08.domain.order.dto.OrderResponse;
import com.test08.domain.order.entity.Order;
import com.test08.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderForm orderForm) {
        Order order = orderService.saveOrder(orderForm);
        return ResponseEntity.ok(OrderResponse.from(order));
    }
}
