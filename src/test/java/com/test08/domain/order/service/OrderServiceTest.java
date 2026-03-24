package com.test08.domain.order.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.test08.domain.order.dto.OrderForm;
import com.test08.domain.order.entity.Order;
import com.test08.domain.order.entity.OrderStatus;
import com.test08.domain.order.repository.OrderRepository;
import com.test08.domain.orderitem.entity.OrderItem;
import com.test08.domain.orderitem.repository.OrderItemRepository;
import com.test08.domain.product.entity.Product;
import com.test08.domain.product.repository.ProductRepository;
import java.util.List;
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
    private OrderItemRepository orderItemRepository;

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
                Map.of(1, 2)  // 상품1번 2개 (Integer 타입!)
        );

        Product product = new Product(1, "Columbia Nariño", 5000, "img.jpg", "커피콩");
        Order newOrder = Order.create(form.email(), form.address(), form.postCode());

        given(orderRepository.findByEmailAndAddressAndStatus(any(), any(), any()))
                .willReturn(Optional.empty());  // 기존 주문 없음
        given(orderRepository.save(any())).willReturn(newOrder);
        given(productRepository.findById(1)).willReturn(Optional.of(product));
        given(orderItemRepository.findAllByOrder(any())).willReturn(List.of());

        // when
        Order result = orderService.saveOrder(form);

        // then
        assertThat(result.getEmail()).isEqualTo("test@test.com");
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PENDING);
        verify(orderRepository).save(any());  // save 호출됐는지 확인
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
        OrderItem existingItem = OrderItem.create(existingOrder, product, 2);  // 이미 2개

        given(orderRepository.findByEmailAndAddressAndStatus(any(), any(), any()))
                .willReturn(Optional.of(existingOrder));  // 기존 주문 있음
        given(productRepository.findById(1)).willReturn(Optional.of(product));
        given(orderItemRepository.findAllByOrder(any())).willReturn(List.of(existingItem));

        // when
        orderService.saveOrder(form);

        // then
        assertThat(existingItem.getQuantity()).isEqualTo(3);  // 2 + 1 = 3
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
        OrderItem existingItem = OrderItem.create(existingOrder, product1, 2);  // 상품1번 2개

        given(orderRepository.findByEmailAndAddressAndStatus(any(), any(), any()))
                .willReturn(Optional.of(existingOrder));
        given(productRepository.findById(2)).willReturn(Optional.of(product2));
        given(orderItemRepository.findAllByOrder(any()))
                .willReturn(List.of(existingItem));  // 기존엔 상품1번만 있음

        // when
        orderService.saveOrder(form);

        // then
        // 상품1번은 그대로 2개
        assertThat(existingItem.getQuantity()).isEqualTo(2);

        // 상품2번은 1개로 새로 저장됐는지 확인
        verify(orderItemRepository).save(argThat(item ->
                item.getProduct().getProductId() == 2
                        && item.getQuantity() == 1
        ));
    }
}