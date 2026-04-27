package com.example.shop.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddProductImageRequest {

    @NotBlank(message = "Image URL must not be blank")
    @URL(message = "Image URL must be valid")
    String imageUrl;

    String altText;

    @Min(value = 0, message = "Position must be greater than or equal to 0")
    int position;
}
