package com.test08.domain.order.service;

import com.test08.domain.order.dto.OrderForm;
import com.test08.domain.order.dto.OrderResponse;
import com.test08.domain.order.entity.Order;
import com.test08.domain.order.entity.OrderStatus;
import com.test08.domain.order.repository.OrderRepository;
import com.test08.domain.product.entity.Product;
import com.test08.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order saveOrder(OrderForm form) {
        Order order = orderRepository
                .findByEmailAndAddressAndStatus(form.email(), form.address(), OrderStatus.PENDING)
                .orElseGet(() -> orderRepository.save(
                        Order.create(form.email(), form.address(), form.postCode())
                ));

        for (var entry : form.items().entrySet()) {
            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("상품 없음 : " + entry.getKey()));
            order.addOrMergeItem(product, entry.getValue());
        }

        order.recalculateTotalPrice();
        return order;
    }

    @Transactional
    public void modifyStatus() {
        List<Order> orders = orderRepository.findByStatus(OrderStatus.PENDING);

        for (Order order : orders) {
            order.modifyOrderStatus();
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findOrdersByEmail(String email) {
        return orderRepository.findByEmailOrderByUpdatedTimeDesc(email).stream()
                .map(OrderResponse::from)
                .toList();
    }
}
