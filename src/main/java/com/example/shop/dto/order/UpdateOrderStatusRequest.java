package com.example.shop.dto.order;

import com.example.shop.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateOrderStatusRequest {

    @NotNull(message = "Status must not be null")
    private OrderStatus status;

}
