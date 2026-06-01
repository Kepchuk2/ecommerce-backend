package com.example.shop.dto.product;

import java.math.BigDecimal;

public record ProductVariantResponse(
    Long id,
    String sku,
    BigDecimal price,
    String size,
    String color
){}
