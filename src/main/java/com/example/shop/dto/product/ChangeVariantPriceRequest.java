package com.example.shop.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ChangeVariantPriceRequest(
    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal newPrice
){}
