package com.test08.domain.orderitem.entity;

import com.test08.domain.order.entity.Order;
import com.test08.domain.product.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    private int quantity;
    private int price;
    private LocalDateTime orderTime;

    public static OrderItem create (Order order, Product product, int quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.order = order;
        orderItem.product = product;
        orderItem.quantity = quantity;
        orderItem.price = product.getPrice();
        orderItem.orderTime = LocalDateTime.now();
        return orderItem;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }
}
