package com.example.shop.dto.order;

public record CreateOrderRequest(
    Long userId,
    String sessionId
){}
