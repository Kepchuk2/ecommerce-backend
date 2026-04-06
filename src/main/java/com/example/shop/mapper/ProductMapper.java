package com.example.shop.mapper;

import com.example.shop.dto.product.ProductImageResponse;
import com.example.shop.dto.product.ProductResponse;
import com.example.shop.dto.product.ProductVariantResponse;
import com.example.shop.entity.Product;
import com.example.shop.entity.ProductImage;
import com.example.shop.entity.ProductVariant;

import java.util.ArrayList;
import java.util.List;

public final class ProductMapper {

    public static ProductVariantResponse toProductVariantResponse(ProductVariant variant) {
        if (variant == null) {
            return null;
        }

        ProductVariantResponse response = new ProductVariantResponse();

        response.setId(variant.getId());
        response.setSku(variant.getSku());
        response.setPrice(variant.getPrice());
        response.setSize(variant.getSize());
        response.setColor(variant.getColor());

        return  response;
    }

    public static List<ProductVariantResponse> toProductVariantResponseList(List<ProductVariant> variants) {
        List<ProductVariantResponse> responses = new ArrayList<>();

        if (variants == null) {
            return responses;
        }

        for (ProductVariant variant : variants) {
            if (variant != null) {
                responses.add(toProductVariantResponse(variant));
            }
        }

        return responses;
    }

    public static ProductImageResponse toProductImageResponse(ProductImage image) {
        if (image == null) {
            return null;
        }

        ProductImageResponse response = new ProductImageResponse();

        response.setId(image.getId());
        response.setImageUrl(image.getImageUrl());
        response.setAltText(image.getAltText());
        response.setPosition(image.getPosition());

        return response;
    }

    public static List<ProductImageResponse> toProductImageResponseList(List<ProductImage> images) {
        List<ProductImageResponse> responses = new ArrayList<>();

        if (images == null) {
            return responses;
        }

        for (ProductImage image : images) {
            if (image != null) {
                responses.add(toProductImageResponse(image));
            }
        }

        return responses;
    }

    public static ProductResponse toProductResponse(Product product) {
        if (product == null) {
            return null;
        }

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setDescription(product.getDescription());
        response.setName(product.getName());
        response.setCategory(product.getCategory());
        response.setImages(toProductImageResponseList(product.getProductImages()));
        response.setVariants(toProductVariantResponseList(product.getVariants()));

        return response;
    }
}
