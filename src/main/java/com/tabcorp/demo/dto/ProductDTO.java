package com.tabcorp.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductDTO {
    @NotBlank(message = "Product code is required")
    private String productCode;
}
