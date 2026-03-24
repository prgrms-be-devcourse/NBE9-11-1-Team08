package com.test08.domain.order.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.test08.domain.order.dto.OrderForm;
import com.test08.domain.order.entity.Order;
import com.test08.domain.order.entity.OrderStatus;
import com.test08.domain.order.repository.OrderRepository;
import com.test08.domain.product.entity.Product;
import com.test08.domain.product.repository.ProductRepository;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("새 주문 생성 - 기존 주문 없을 때")
    void saveOrder_newOrder() {
        // given
        OrderForm form = new OrderForm(
                "test@test.com",
                "서울시 강남구",
                "12345",
                Map.of(1, 2)
        );

        Product product = new Product(1, "Columbia Nariño", 5000, "img.jpg", "커피콩");
        Order newOrder = Order.create(form.email(), form.address(), form.postCode());

        given(orderRepository.findByEmailAndAddressAndStatus(any(), any(), any()))
                .willReturn(Optional.empty());
        given(orderRepository.save(any())).willReturn(newOrder);
        given(productRepository.findById(1)).willReturn(Optional.of(product));

        // when
        Order result = orderService.saveOrder(form);

        // then
        assertThat(result.getEmail()).isEqualTo("test@test.com");
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PENDING);
        verify(orderRepository).save(any());
    }

    @Test
    @DisplayName("합배송 - 같은 이메일+주소 기존 주문 있을 때")
    void saveOrder_mergeOrder() {
        // given
        OrderForm form = new OrderForm(
                "test@test.com",
                "서울시 강남구",
                "12345",
                Map.of(1, 1)  // 상품1번 1개 추가
        );

        Product product = new Product(1, "Columbia Nariño", 5000, "img.jpg", "커피콩");
        Order existingOrder = Order.create("test@test.com", "서울시 강남구", "12345");
        existingOrder.addOrMergeItem(product, 2);  // 이미 2개 담긴 상태

        given(orderRepository.findByEmailAndAddressAndStatus(any(), any(), any()))
                .willReturn(Optional.of(existingOrder));
        given(productRepository.findById(1)).willReturn(Optional.of(product));

        // when
        orderService.saveOrder(form);

        // then
        assertThat(existingOrder.getOrderItems().get(0).getQuantity()).isEqualTo(3);  // 2 + 1 = 3
    }

    @Test
    @DisplayName("합배송 - 다른 상품 추가될 때")
    void saveOrder_mergeOrder_newProduct() {
        // given
        OrderForm form = new OrderForm(
                "test@test.com",
                "서울시 강남구",
                "12345",
                Map.of(2, 1)  // 상품2번 1개 추가
        );

        Product product1 = new Product(1, "Columbia Nariño", 5000, "img.jpg", "커피콩");
        Product product2 = new Product(2, "Brazil Serra", 6000, "img.jpg", "커피콩");
        Order existingOrder = Order.create("test@test.com", "서울시 강남구", "12345");
        existingOrder.addOrMergeItem(product1, 2);  // 상품1번 2개 담긴 상태

        given(orderRepository.findByEmailAndAddressAndStatus(any(), any(), any()))
                .willReturn(Optional.of(existingOrder));
        given(productRepository.findById(2)).willReturn(Optional.of(product2));

        // when
        orderService.saveOrder(form);

        // then
        assertThat(existingOrder.getOrderItems().stream()
                .anyMatch(item -> item.getProduct().getProductId() == 1 && item.getQuantity() == 2))
                .isTrue();  // 상품1번은 그대로 2개
        assertThat(existingOrder.getOrderItems().stream()
                .anyMatch(item -> item.getProduct().getProductId() == 2 && item.getQuantity() == 1))
                .isTrue();
    }
}
