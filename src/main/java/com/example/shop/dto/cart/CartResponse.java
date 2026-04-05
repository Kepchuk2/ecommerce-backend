package com.example.shop.dto.cart;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class CartResponse {

    private Long id;
    private String sessionId;
    private BigDecimal totalPrice;
    private List<CartItemResponse> items;
}
