package com.example.shop.dto.order;

import java.math.BigDecimal;

public record OrderItemResponse(
     Long id,
     String sku,
     Long variantId,
     BigDecimal price,
     String productName,
     Integer quantity,
     String size,
     String color,
     BigDecimal totalPrice
){}
