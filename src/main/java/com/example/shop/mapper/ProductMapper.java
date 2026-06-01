package com.example.shop.mapper;

import com.example.shop.dto.product.ProductImageResponse;
import com.example.shop.dto.product.ProductListResponse;
import com.example.shop.dto.product.ProductResponse;
import com.example.shop.dto.product.ProductVariantResponse;
import com.example.shop.entity.Product;
import com.example.shop.entity.ProductImage;
import com.example.shop.entity.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "images", source = "productImages")
    ProductResponse toProductResponse(Product product);

    ProductVariantResponse toProductVariantResponse(ProductVariant variant);

    List<ProductVariantResponse> toProductVariantResponseList(Collection<ProductVariant> variants);

    ProductImageResponse toProductImageResponse(ProductImage image);

    List<ProductImageResponse> toProductImageResponseList(List<ProductImage> images);

    List<ProductResponse> toProductResponseList(List<Product> products);

    @Mapping(target = "images", source = "productImages")
    ProductListResponse toProductListResponse(Product product);

    List<ProductListResponse> toProductListResponseList(List<Product> products);

}
