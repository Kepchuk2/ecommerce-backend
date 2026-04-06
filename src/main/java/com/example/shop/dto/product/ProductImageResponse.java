package com.example.shop.dto.product;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImageResponse {

    Long id;
    String imageUrl;
    String altText;
    int position;

}
