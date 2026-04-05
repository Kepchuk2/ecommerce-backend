package com.example.shop.dto.order;

import com.example.shop.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateOrderStatusRequest {

    private OrderStatus status;

}
