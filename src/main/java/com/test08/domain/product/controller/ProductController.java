package com.test08.domain.product.controller;

import com.test08.domain.product.dto.ProductRequest;
import com.test08.domain.product.dto.ProductResponse;
import com.test08.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.findAllProducts();
    }

    // 상품 조회 시 productId를 경로 변수로 받아서 해당 상품을 조회하도록 수정
    @GetMapping("/{productId}")
    public ProductResponse getProduct(@PathVariable int productId) {
        return productService.findProduct(productId);
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