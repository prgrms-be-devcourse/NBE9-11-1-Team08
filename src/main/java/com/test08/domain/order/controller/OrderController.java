package com.test08.domain.order.controller;

import com.test08.domain.order.dto.OrderForm;
import com.test08.domain.order.dto.OrderResponse;
import com.test08.domain.order.entity.Order;
import com.test08.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderForm orderForm) {
        Order order = orderService.saveOrder(orderForm);
        return ResponseEntity.ok(OrderResponse.from(order));
    }

    // 전체 주문 관리 (관리자)
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(@RequestParam(required = false) String email) {
        if (email != null && !email.isBlank()) {
            return ResponseEntity.ok(orderService.findOrdersByEmail(email));
        }

        return ResponseEntity.ok(orderService.findAllOrders());
    }
}
