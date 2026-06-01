package com.example.shop.dto.order;

import com.example.shop.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest(
    @NotNull(message = "Status must not be null")
    OrderStatus status
){}
