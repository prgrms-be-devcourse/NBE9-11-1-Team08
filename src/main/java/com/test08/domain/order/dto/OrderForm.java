package com.test08.domain.order.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Map;

public record OrderForm(
        @Email @NotBlank String email,
        @NotBlank String address,
        @Pattern(regexp = "\\d{5}") String postCode,
        Map<Integer, Integer> items
) {
}
