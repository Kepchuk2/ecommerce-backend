package com.example.shop.dto.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse {

     Long id;
     String sku;
     Long variantId;
     BigDecimal price;
     String productName;
     Integer quantity;
     String size;
     String color;
     BigDecimal totalPrice;
}
