package com.test08.domain.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    private String imageUrl;

    @Column(length = 50)
    private String category;

    public Product(String name, int price, String imageUrl, String category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    } // 생성자 추가 productRequest에서 받아서 product 엔티티로 변환 4개의 필드만 받는 생성자 (productId는 자동으로 생성되서 제외)

    public void update(String name, int price, String imageUrl, String category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    } // 상품 정보 수정을 위한 메서드 추가
}