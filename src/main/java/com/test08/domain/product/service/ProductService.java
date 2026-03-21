package com.test08.domain.product.service;

import com.test08.domain.product.dto.ProductRequest;
import com.test08.domain.product.dto.ProductResponse;
import com.test08.domain.product.entity.Product;
import com.test08.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public void createProduct(ProductRequest productRequest) {
        Product product = new Product(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl(),
                productRequest.getCategory()
        );

        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        return new ProductResponse(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory()
        );
    }

    @Transactional
    public void updateProduct(int productId, ProductRequest productRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("수정할 상품이 없습니다."));
        product.update(productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl(),
                productRequest.getCategory());
    }

    @Transactional
    public void deleteProduct(int productId) {
        productRepository.deleteById(productId);
    }

}