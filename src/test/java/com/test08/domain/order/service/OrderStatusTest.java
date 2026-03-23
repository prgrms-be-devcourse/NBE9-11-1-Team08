package com.test08.domain.order.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.test08.domain.order.entity.Order;
import com.test08.domain.order.entity.OrderStatus;
import com.test08.domain.order.repository.OrderRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderStatusTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("status 변경 (PENDING -> SHIPPED ")
    void t1() {
        // 1. PENDING 상태 주문 생성 후 DB 저장
        Order order = Order.create("test@test.com", "서울시 강남구", "12345");
        orderRepository.save(order);

        // 저장됐는지 확인
        List<Order> pending = orderRepository.findByStatus(OrderStatus.PENDING);
        System.out.println("PENDING 주문 수: " + pending.size());

        // 2. modifyStatus() 호출
        orderService.modifyStatus();

        // 호출 후 확인
        List<Order> shipped = orderRepository.findByStatus(OrderStatus.SHIPPED);
        System.out.println("SHIPPED 주문 수: " + shipped.size());

        // 3. 검증
        assertThat(shipped).isNotEmpty();
    }
}
