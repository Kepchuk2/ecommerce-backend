package com.example.shop.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddProductVariantRequest {

    @NotBlank(message = "SKU must not be blank")
    String sku;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal price;

    String size;
    String color;
}
