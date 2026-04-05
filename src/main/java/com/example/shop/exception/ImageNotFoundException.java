package com.example.shop.exception;

public class ImageNotFoundException extends RuntimeException {
    public ImageNotFoundException(Long imageId) {
        super("Image not found with id: " + imageId);
    }
}
