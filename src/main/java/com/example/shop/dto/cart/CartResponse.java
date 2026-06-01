package com.example.shop.dto.cart;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
     Long id,
     String sessionId,
     BigDecimal totalPrice,
     List<CartItemResponse> items
){}
