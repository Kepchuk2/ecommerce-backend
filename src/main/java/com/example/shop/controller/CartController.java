package com.example.shop.controller;

import com.example.shop.dto.cart.AddCartItemRequest;
import com.example.shop.dto.cart.CartResponse;
import com.example.shop.dto.cart.UpdateCartItemQuantityRequest;
import com.example.shop.entity.Cart;
import com.example.shop.mapper.CartMapper;
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
        Cart cart = cartService.findOrCreateCartBySessionId(sessionId);
        return CartMapper.toCartResponse(cart);
    }

    @PostMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponse createUserCart(@PathVariable Long userId) {
        Cart cart = cartService.findOrCreateCartByUserId(userId);
        return CartMapper.toCartResponse(cart);
    }

    @PostMapping("/guest/items")
    public CartResponse addItemToGuestCart(@RequestHeader("X-Session-Id") String sessionId, @Valid @RequestBody AddCartItemRequest request) {
        Cart cart = cartService.addProductToCartBySessionId(sessionId, request.getVariantId(), request.getQuantity());
        return CartMapper.toCartResponse(cart);
    }

    @PutMapping("/guest/items/{variantId}")
    public CartResponse updateGuestCartItemQuantity(@RequestHeader("X-Session-Id") String sessionId, @PathVariable Long variantId , @Valid @RequestBody UpdateCartItemQuantityRequest request) {
        Cart cart = cartService.updateCartItemQuantityBySessionId(sessionId, variantId ,request.getQuantity());
        return CartMapper.toCartResponse(cart);
    }

    @DeleteMapping("/guest/items/{variantId}")
    public CartResponse removeItemFromGuestCart(@RequestHeader("X-Session-Id") String sessionId, @PathVariable Long variantId) {
        Cart cart = cartService.removeItemFromCartBySessionId(sessionId, variantId);
        return CartMapper.toCartResponse(cart);
    }

    @DeleteMapping("/guest")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearGuestCart(@RequestHeader("X-Session-Id") String sessionId) {
        cartService.clearCartBySessionId(sessionId);
    }

    @GetMapping("/user/{userId}")
    public CartResponse getCartByUserId(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return CartMapper.toCartResponse(cart);
    }

    @PostMapping("/user/{userId}/items")
    public CartResponse addItemToUserCart(@PathVariable Long userId, @Valid @RequestBody AddCartItemRequest request) {
        Cart cart = cartService.addProductToCartByUserId(userId, request.getVariantId(), request.getQuantity());
        return CartMapper.toCartResponse(cart);
    }

    @PutMapping("/user/{userId}/items/{variantId}")
    public CartResponse updateUserCartItemQuantity(@PathVariable Long userId, @PathVariable Long variantId, @Valid @RequestBody UpdateCartItemQuantityRequest request) {
        Cart cart = cartService.updateCartItemQuantityByUserId(userId, variantId, request.getQuantity());
        return CartMapper.toCartResponse(cart);
    }

    @DeleteMapping("/user/{userId}/items/{variantId}")
    public CartResponse removeItemFromUserCart(@PathVariable Long userId, @PathVariable Long variantId) {
        Cart cart = cartService.removeItemFromCartByUserId(userId, variantId);
        return CartMapper.toCartResponse(cart);
    }

    @DeleteMapping("/user/{userId}")
    public CartResponse clearUserCart(@PathVariable Long userId) {
        Cart cart = cartService.clearCartByUserId(userId);
        return CartMapper.toCartResponse(cart);
    }
}
