package com.masoko.store.features.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

// serialization
public class ProductResponseDto {

    private Long id;
    private String name;
    private String slug;

    @JsonProperty("display_description")
    private String description;

    @JsonProperty("price")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal discountedPrice;

    @JsonIgnore
    private BigDecimal systemInternalWholesaleCost;
}