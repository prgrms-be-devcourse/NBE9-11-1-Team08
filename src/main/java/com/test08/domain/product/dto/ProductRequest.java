package com.test08.domain.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotBlank(message = "상품명을 입력해주세요.")
    private String name;

    @Min(value = 1, message = "가격을 1원 이상으로 입력해주세요.")
    private int price;

    private String imageUrl;
    private String category;
}