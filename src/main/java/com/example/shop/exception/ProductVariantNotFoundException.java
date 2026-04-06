package com.example.shop.exception;

public class ProductVariantNotFoundException extends RuntimeException {
    public ProductVariantNotFoundException(String sku) {
        super("Product variant with id " + sku + " not found");
    }
}
