package com.example.shop.dto.order;

import com.example.shop.entity.Currency;
import com.example.shop.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
     Long id,
     Long userId,
     OrderStatus status,
     BigDecimal totalPrice,
     Currency currency,
     String deliveryAddress,
     String deliveryMethod,
     String trackingNumber,
     LocalDateTime createdAt,
     LocalDateTime updatedAt,
     List<OrderItemResponse> items
) {}
