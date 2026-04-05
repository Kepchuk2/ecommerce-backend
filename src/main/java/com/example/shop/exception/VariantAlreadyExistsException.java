package com.example.shop.exception;

import lombok.Getter;

@Getter
public class VariantAlreadyExistsException extends RuntimeException {
    private final String sku;

    public VariantAlreadyExistsException(String sku) {
        super("Product variant with SKU '" + sku + "' already exists");
        this.sku = sku;
    }

}
