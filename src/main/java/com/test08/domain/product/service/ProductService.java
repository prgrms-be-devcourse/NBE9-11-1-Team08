package com.test08.domain.product.service;

import com.test08.domain.product.dto.ProductRequest;
import com.test08.domain.product.dto.ProductResponse;
import com.test08.domain.product.entity.Product;
import com.test08.domain.product.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product(
                request.category(),
                request.name(),
                request.price(),
                request.imageUrl()
        );
        return ProductResponse.from(productRepository.save(product));
    }
}
