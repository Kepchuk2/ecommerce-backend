package com.example.shop.dto.product;

import com.example.shop.entity.ProductCategory;
import com.example.shop.entity.ProductImage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {

    Long id;
    String description;
    String name;
    ProductCategory category;
    List<ProductImageResponse> images;
    Set<ProductVariantResponse> variants;
}
