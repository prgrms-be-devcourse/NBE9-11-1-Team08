package com.test08.domain.product.repository;

import com.test08.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    // productId int라 Integer로 변경

}