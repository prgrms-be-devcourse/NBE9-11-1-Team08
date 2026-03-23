package com.test08.domain.order.entity;

import com.test08.domain.orderitem.entity.OrderItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Table(name = "orders")
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String email;
    private String address;
    private String postCode;
    private LocalDateTime firstOrderTime;
    private LocalDateTime updatedTime;
    private int totalPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    public static Order create(String email, String address, String postCode) {
        Order order = new Order();
        order.email = email;
        order.address = address;
        order.postCode = postCode;
        order.firstOrderTime = LocalDateTime.now();
        order.updatedTime = LocalDateTime.now();
        order.totalPrice = 0;
        order.status = OrderStatus.PENDING;
        return order;
    }

    public void updateTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void updateTime() {
        this.updatedTime = LocalDateTime.now();
    }
}
