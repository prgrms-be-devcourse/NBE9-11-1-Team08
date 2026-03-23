package com.test08.domain.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(length = 100)
    private String email;

    @Column(length = 255)
    private String address;

    private String postCode;

    private LocalDateTime firstOrderTime;

    private LocalDateTime updatedTime;

    private int totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public void modifyOrderStatus() {
        this.status = OrderStatus.SHIPPED;
        this.updatedTime = LocalDateTime.now();
    }
}