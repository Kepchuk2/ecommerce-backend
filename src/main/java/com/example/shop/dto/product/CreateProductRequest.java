package com.example.shop.dto.product;

import com.example.shop.entity.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductRequest {

    @NotBlank(message = "Name must not be blank")
    String name;

    @NotBlank(message = "Description must not be blank")
    String description;

    @NotNull(message = "Category must not be null")
    ProductCategory category;
}
