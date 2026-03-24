package com.test08.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrderService orderService;

    @Scheduled(cron = "0 0 14 * * *")
    public void processShipping() {
        orderService.modifyStatus();
    }
}
