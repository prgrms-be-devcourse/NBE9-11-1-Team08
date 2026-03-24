package com.test08.domain.order.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.test08.domain.order.entity.Order;
import com.test08.domain.order.entity.OrderStatus;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("이메일+주소+PENDING 조건으로 주문 조회 성공")
    void findByEmailAndAddressAndStatus_pending() {
        // given
        Order order = Order.create("test@test.com", "서울시 강남구", "12345");
        orderRepository.save(order);

        // when
        Optional<Order> result = orderRepository
                .findByEmailAndAddressAndStatus("test@test.com", "서울시 강남구", OrderStatus.PENDING);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@test.com");
        assertThat(result.get().getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("SHIPPED 상태인 주문은 PENDING 조회 시 안됨")
    void findByEmailAndAddressAndStatus_shipped() {
        // given
        Order order = Order.create("test@test.com", "서울시 강남구", "12345");
        orderRepository.save(order);

        // when: SHIPPED로 조회 시도
        Optional<Order> result = orderRepository
                .findByEmailAndAddressAndStatus("test@test.com", "서울시 강남구", OrderStatus.SHIPPED);

        // then: PENDING 상태라서 SHIPPED 조회엔 안 나와야 함
        assertThat(result).isEmpty();
    }
}