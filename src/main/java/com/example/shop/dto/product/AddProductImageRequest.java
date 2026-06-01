package com.example.shop.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record AddProductImageRequest(
    @NotBlank(message = "Image URL must not be blank")
    @URL(message = "Image URL must be valid")
    String imageUrl,

    String altText,

    @Min(value = 0, message = "Position must be greater than or equal to 0")
    int position
){}
