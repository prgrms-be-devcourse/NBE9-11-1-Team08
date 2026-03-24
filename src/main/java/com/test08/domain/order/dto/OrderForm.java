package com.test08.domain.order.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.Map;

public record OrderForm(
        @Email @NotBlank String email,
        @NotBlank String address,
        @Pattern(regexp = "\\d{5}", message = "우편번호는 5자리 숫자여야 합니다") String postCode,
        @NotEmpty Map<Integer, Integer> items
) {
}
