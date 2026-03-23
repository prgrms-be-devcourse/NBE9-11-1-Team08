package com.test08.domain.order.service;

import com.test08.domain.order.dto.OrderForm;
import com.test08.domain.order.entity.Order;
import com.test08.domain.order.entity.OrderStatus;
import com.test08.domain.order.repository.OrderRepository;
import com.test08.domain.orderitem.entity.OrderItem;
import com.test08.domain.orderitem.repository.OrderItemRepository;
import com.test08.domain.product.entity.Product;
import com.test08.domain.product.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order saveOrder(OrderForm form) {
        Order order = orderRepository
                .findByEmailAndAddressAndStatus(form.email(), form.address(), OrderStatus.PENDING)
                .orElseGet(() -> orderRepository.save(
                        Order.create(form.email(), form.address(), form.postCode())
                ));

        List<OrderItem> existingItems = orderItemRepository.findAllByOrder(order);

        for (var entry : form.items().entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("상품 없음 : " + productId));

            existingItems.stream()
                    .filter(item -> item.getProduct().getProductId() == productId)
                    .findFirst()
                    .ifPresentOrElse(
                            item -> item.addQuantity(quantity),
                            () -> orderItemRepository.save(
                                    OrderItem.create(order, product, quantity))
                    );
        }

        List<OrderItem> allItems = orderItemRepository.findAllByOrder(order);
        int total = allItems.stream()
                .mapToInt(i -> i.getPrice() * i.getQuantity())
                .sum();
        order.updateTotalPrice(total);

        return order;
    }

    @Transactional
    public void modifyStatus() {
        List<Order> orders = orderRepository.findByStatus(OrderStatus.PENDING);

        for (Order order : orders) {
            order.modifyOrderStatus();
        }
    }
}

