package com.example.shop.dto.product;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantResponse {

    Long id;
    String sku;
    BigDecimal price;
    String size;
    String color;
}
