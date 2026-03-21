package com.test08.domain.product.controller;

import com.test08.domain.product.dto.ProductRequest;
import com.test08.domain.product.dto.ProductResponse;
import com.test08.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products") // 공통 경로 설정
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public String createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
        return "상품 등록 완료";
    }


    @GetMapping("/{productId}")
    public ProductResponse getProduct(@PathVariable int productId) {
        return productService.findById(productId);
    }

    @PutMapping("/{productId}")
    public String updateProduct(@PathVariable int productId, @RequestBody ProductRequest productRequest) {
        productService.updateProduct(productId, productRequest);
        return "상품 수정 완료";
    }

    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable int productId) {
        productService.deleteProduct(productId);
        return "상품 삭제 완료";
    }
}