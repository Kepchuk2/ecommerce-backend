package com.example.shop.controller;

import com.example.shop.dto.cart.AddCartItemRequest;
import com.example.shop.dto.cart.CartResponse;
import com.example.shop.dto.cart.UpdateCartItemQuantityRequest;
import com.example.shop.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/guest")
    public CartResponse getGuestCartBySessionId(@RequestHeader("X-Session-Id") String sessionId) {
        return cartService.findOrCreateCartBySessionId(sessionId);
    }

    @PostMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponse createUserCart(@PathVariable Long userId) {
        return cartService.findOrCreateCartByUserId(userId);
    }

    @PostMapping("/guest/items")
    public CartResponse addItemToGuestCart(@RequestHeader("X-Session-Id") String sessionId, @Valid @RequestBody AddCartItemRequest request) {
        return cartService.addProductToCartBySessionId(sessionId, request.variantId(), request.quantity());
    }

    @PutMapping("/guest/items/{variantId}")
    public CartResponse updateGuestCartItemQuantity(@RequestHeader("X-Session-Id") String sessionId, @PathVariable Long variantId , @Valid @RequestBody UpdateCartItemQuantityRequest request) {
        return cartService.updateCartItemQuantityBySessionId(sessionId, variantId ,request.quantity());
    }

    @DeleteMapping("/guest/items/{variantId}")
    public CartResponse removeItemFromGuestCart(@RequestHeader("X-Session-Id") String sessionId, @PathVariable Long variantId) {
        return cartService.removeItemFromCartBySessionId(sessionId, variantId);
    }

    @DeleteMapping("/guest")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearGuestCart(@RequestHeader("X-Session-Id") String sessionId) {
        cartService.clearCartBySessionId(sessionId);
    }

    @GetMapping("/user/{userId}")
    public CartResponse getCartByUserId(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping("/user/{userId}/items")
    public CartResponse addItemToUserCart(@PathVariable Long userId, @Valid @RequestBody AddCartItemRequest request) {
        return cartService.addProductToCartByUserId(userId, request.variantId(), request.quantity());
    }

    @PutMapping("/user/{userId}/items/{variantId}")
    public CartResponse updateUserCartItemQuantity(@PathVariable Long userId, @PathVariable Long variantId, @Valid @RequestBody UpdateCartItemQuantityRequest request) {
        return cartService.updateCartItemQuantityByUserId(userId, variantId, request.quantity());
    }

    @DeleteMapping("/user/{userId}/items/{variantId}")
    public CartResponse removeItemFromUserCart(@PathVariable Long userId, @PathVariable Long variantId) {
        return cartService.removeItemFromCartByUserId(userId, variantId);
    }

    @DeleteMapping("/user/{userId}")
    public CartResponse clearUserCart(@PathVariable Long userId) {
        return cartService.clearCartByUserId(userId);
    }
}
