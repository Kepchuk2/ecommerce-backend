package com.example.shop.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String sessionId) {
        super("Cart not found: " + sessionId);
    }

    public CartNotFoundException(Long userId) {
        super("Cart not found: " + userId);
    }
}
