package com.example.shop.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AddProductVariantRequest(
    @NotBlank(message = "SKU must not be blank")
    String sku,

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal price,

    String size,
    String color
){}
