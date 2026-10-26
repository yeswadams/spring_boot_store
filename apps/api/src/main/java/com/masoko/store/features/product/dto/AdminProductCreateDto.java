package com.masoko.store.features.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

// deserialization
public class AdminProductCreateDto {

    @NotBlank(message = "Product name cannot be blank")
    private String name;

    @NotBlank(message = " Description is required")
    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;

    @NotNull(message = "Actual price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    @JsonProperty("actual_price")
    private BigDecimal actualPrice;

    @NotNull(message = "Discount price is required")
    @JsonProperty("discounted_price")
    private BigDecimal discountedPrice;
}