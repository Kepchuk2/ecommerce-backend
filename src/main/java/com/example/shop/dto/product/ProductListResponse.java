package com.example.shop.dto.product;

import com.example.shop.entity.ProductCategory;
import java.util.List;

public record ProductListResponse( 
    Long id,
    String name,
    String description,
    ProductCategory category,
    List<ProductImageResponse> images
){}
