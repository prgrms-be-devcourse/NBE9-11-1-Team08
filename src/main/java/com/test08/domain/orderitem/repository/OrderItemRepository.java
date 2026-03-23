package com.test08.domain.orderitem.repository;

import com.test08.domain.order.entity.Order;
import com.test08.domain.orderitem.entity.OrderItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findAllByOrder(Order order);
}
