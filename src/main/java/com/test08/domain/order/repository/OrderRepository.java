package com.test08.domain.order.repository;

import com.test08.domain.order.entity.Order;
import com.test08.domain.order.entity.OrderStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);

    Optional<Order> findByEmailAndAddressAndStatus(String email, String address, OrderStatus status);
}
