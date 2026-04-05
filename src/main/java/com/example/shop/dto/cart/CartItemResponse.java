package com.example.shop.dto.cart;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor
public class CartItemResponse {

    private Long id;
    private Long variantId;
    private String sku;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private String size;
    private String color;
    private BigDecimal totalPrice;
//    private String imageUrl;

}
