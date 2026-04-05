package com.example.shop.dto.cart;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


@Getter @Setter
@NoArgsConstructor

public class UpdateCartItemQuantityRequest {

    @NotNull
    @Min(0)
    private Integer quantity;
}
