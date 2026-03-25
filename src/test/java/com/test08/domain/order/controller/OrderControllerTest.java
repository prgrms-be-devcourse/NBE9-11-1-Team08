package com.test08.domain.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test08.domain.order.dto.OrderForm;
import com.test08.domain.order.dto.OrderResponse;
import com.test08.domain.order.entity.Order;
import com.test08.domain.order.entity.OrderStatus;
import com.test08.domain.order.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //    @MockBean
    // 스프링 프레임워크 업데이트로 인한 교체
    @MockitoBean
    private OrderService orderService;

    @Test
    @DisplayName("유효한 요청 - 주문 생성 성공")
    void createOrder_validRequest() throws Exception {
        OrderForm form = new OrderForm("test@test.com", "서울시 강남구", "12345", Map.of(1, 2));
        Order order = Order.create(form.email(), form.address(), form.postCode());

        given(orderService.saveOrder(any())).willReturn(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("이메일 형식 오류 - 400 반환")
    void createOrder_invalidEmail() throws Exception {
        OrderForm form = new OrderForm("invalid-email", "서울시 강남구", "12345", Map.of(1, 2));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    @DisplayName("이메일 빈 값 - 400 반환")
    void createOrder_blankEmail() throws Exception {
        OrderForm form = new OrderForm("", "서울시 강남구", "12345", Map.of(1, 2));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    @DisplayName("주소 빈 값 - 400 반환")
    void createOrder_blankAddress() throws Exception {
        OrderForm form = new OrderForm("test@test.com", "", "12345", Map.of(1, 2));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.address").exists());
    }

    @Test
    @DisplayName("우편번호 5자리 숫자 아님 - 400 반환")
    void createOrder_invalidPostCode() throws Exception {
        OrderForm form = new OrderForm("test@test.com", "서울시 강남구", "1234", Map.of(1, 2));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.postCode").exists());
    }

    @Test
    @DisplayName("우편번호 숫자 아닌 문자 포함 - 400 반환")
    void createOrder_postCodeWithLetters() throws Exception {
        OrderForm form = new OrderForm("test@test.com", "서울시 강남구", "1234a", Map.of(1, 2));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.postCode").exists());
    }

    @Test
    @DisplayName("관리자: 전체 주문 조회 API 호출 성공")
    void getAllOrders_success() throws Exception {
        // given
        OrderResponse.OrderItemDetail itemDetail = new OrderResponse.OrderItemDetail("Columbia Nariño", 2, 5000);
        OrderResponse response = new OrderResponse(
                1L,
                "test@test.com",
                "서울시 강남구",
                "12345",
                10000,
                OrderStatus.PENDING,
                List.of(itemDetail),
                LocalDateTime.now().minusMinutes(5),
                LocalDateTime.now()
        );

        given(orderService.findAllOrders()).willReturn(List.of(response));

        // when & then
        mockMvc.perform(get("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test@test.com"))
                .andExpect(jsonPath("$[0].items[0].name").value("Columbia Nariño")) // 상품 상세 확인
                .andExpect(jsonPath("$[0].updatedTime").exists()); // 수정 시각 확인
    }

    @Test
    @DisplayName("사용자: 이메일 기준 주문 조회 API 호출 성공")
    void getOrdersByEmail_success() throws Exception {
        OrderResponse.OrderItemDetail itemDetail = new OrderResponse.OrderItemDetail("Columbia Nariño", 2, 5000);
        OrderResponse response = new OrderResponse(
                1L,
                "test@test.com",
                "서울시 강남구",
                "12345",
                10000,
                OrderStatus.SHIPPED,
                List.of(itemDetail),
                LocalDateTime.now()
        );

        given(orderService.findOrdersByEmail("test@test.com")).willReturn(List.of(response));

        mockMvc.perform(get("/api/orders")
                        .param("email", "test@test.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test@test.com"))
                .andExpect(jsonPath("$[0].status").value("SHIPPED"));
    }
}
