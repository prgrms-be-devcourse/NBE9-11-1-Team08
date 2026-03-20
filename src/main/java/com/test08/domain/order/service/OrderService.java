package com.test08.domain.order.service;

import com.test08.domain.order.dto.OrderForm;
import com.test08.domain.order.dto.OrderResponse;
import com.test08.domain.order.entity.Order;
import com.test08.domain.order.repository.OrderRepository;
import com.test08.domain.orderitem.entity.OrderItem;
import com.test08.domain.product.entity.Product;
import com.test08.domain.product.repository.ProductRepository;
import com.test08.global.exception.NotFoundException;
import com.test08.global.util.OrderTimeUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public List<OrderResponse> getOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Transactional
    public OrderResponse createOrder(OrderForm form) {
        Order order = new Order(
                form.email(),
                form.address(),
                form.postcode(),
                OrderTimeUtil.now()
        );

        if (form.items() != null) {
            form.items().forEach(item -> {
                Product product = productRepository.findById(item.productId())
                        .orElseThrow(() -> new NotFoundException("Product not found: " + item.productId()));
                order.addOrderItem(new OrderItem(product, item.quantity()));
            });
        }

        return OrderResponse.from(orderRepository.save(order));
    }
}
