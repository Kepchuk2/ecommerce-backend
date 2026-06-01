package com.example.shop.dto.cart;

import java.math.BigDecimal;

public record CartItemResponse(
     Long id,
     Long variantId,
     String sku,
     String productName,
     Integer quantity,
     BigDecimal price,
     String size,
     String color,
     BigDecimal totalPrice
) {}
