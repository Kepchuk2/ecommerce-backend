package com.example.shop.exception;

public class VariantNotFoundException extends RuntimeException {
    public VariantNotFoundException(Long variantId) {
        super("Product variant with id " + variantId + " not found");
    }
    public VariantNotFoundException(String sku) {
        super("Product variant with id " + sku + " not found");
    }
}
