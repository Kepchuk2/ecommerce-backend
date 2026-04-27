package com.example.shop.dto.order;

import com.example.shop.entity.Currency;
import com.example.shop.entity.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {

     Long id;
     Long userId;
     OrderStatus status;
     BigDecimal totalPrice;
     Currency currency;
     String deliveryAddress;
     String deliveryMethod;
     String trackingNumber;
     LocalDateTime createdAt;
     LocalDateTime updatedAt;
     List<OrderItemResponse> items;

}
