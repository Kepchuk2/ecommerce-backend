package com.example.shop.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class AddCartItemRequest {

    @NotNull
    private Long variantId;

    @NotNull
    @Min(1)
    private Integer quantity;

}
