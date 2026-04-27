package com.example.shop.dto.product;

import com.example.shop.entity.ProductCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductListResponse {
    Long id;
    String name;
    String description;
    ProductCategory category;
    List<ProductImageResponse> images;
}
