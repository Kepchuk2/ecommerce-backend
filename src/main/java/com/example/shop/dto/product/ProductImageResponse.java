package com.example.shop.dto.product;

public record ProductImageResponse(
    Long id,
    String imageUrl,
    String altText,
    int position
){}
