package com.test08.domain.orderitem.service;

import com.test08.domain.orderitem.dto.OrderItemResponse;
import com.test08.domain.orderitem.repository.OrderItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public List<OrderItemResponse> getOrderItems() {
        return orderItemRepository.findAll()
                .stream()
                .map(OrderItemResponse::from)
                .toList();
    }
}
