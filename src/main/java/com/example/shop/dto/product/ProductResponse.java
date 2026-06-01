package com.example.shop.dto.product;

import com.example.shop.entity.ProductCategory;
import java.util.List;
import java.util.Set;

public record ProductResponse(
    Long id,
    String description,
    String name,
    ProductCategory category,
    List<ProductImageResponse> images,
    List<ProductVariantResponse> variants
){}
