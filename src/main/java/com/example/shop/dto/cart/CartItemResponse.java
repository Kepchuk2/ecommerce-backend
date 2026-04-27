package com.example.shop.dto.cart;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {

     Long id;
     Long variantId;
     String sku;
     String productName;
     Integer quantity;
     BigDecimal price;
     String size;
     String color;
     BigDecimal totalPrice;

}
