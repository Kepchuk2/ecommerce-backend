package com.example.shop.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor
public class OrderItemResponse {

    private Long id;
    private String sku;
    private Long variantId;
    private BigDecimal price;
    private String productName;
    private Integer quantity;
    private String size;
    private String color;
    private BigDecimal totalPrice;
}
