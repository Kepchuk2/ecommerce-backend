package com.example.shop.controller;

import com.example.shop.dto.cart.AddCartItemRequest;
import com.example.shop.dto.cart.CartResponse;
import com.example.shop.dto.cart.UpdateCartItemQuantityRequest;
import com.example.shop.entity.Cart;
import com.example.shop.mapper.CartMapper;
import com.example.shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/guest/{sessionId}")
    public CartResponse getGuestCartBySessionId(@PathVariable String sessionId) {

        Cart cart = cartService.getCartBySessionId(sessionId);
        return CartMapper.toCartResponse(cart);
    }

    @PostMapping("/guest/{sessionId}/items")
    public CartResponse addItemToGuestCart(@PathVariable String sessionId, @RequestBody AddCartItemRequest request) {

        Cart cart = cartService.addProductToCartBySessionId(sessionId, request.getVariantId(), request.getQuantity());
        return CartMapper.toCartResponse(cart);
    }

    @PutMapping("/guest/{sessionId}/items/{variantId}")
    public CartResponse updateGuestCartItemQuantity(@PathVariable String sessionId,@PathVariable Long variantId ,@RequestBody UpdateCartItemQuantityRequest request) {

        Cart cart = cartService.updateCartItemQuantityBySessionId(sessionId, variantId ,request.getQuantity());
        return CartMapper.toCartResponse(cart);
    }

    @DeleteMapping("/guest/{sessionId}/items/{variantId}")
    public CartResponse removeItemFromGuestCart(@PathVariable String sessionId, @PathVariable Long variantId) {

        Cart cart = cartService.removeItemFromCartBySessionId(sessionId, variantId);
        return CartMapper.toCartResponse(cart);
    }

    @DeleteMapping("/guest/{sessionId}")
    public CartResponse clearGuestCart(@PathVariable String sessionId) {

        Cart cart = cartService.clearCartBySessionId(sessionId);
        return CartMapper.toCartResponse(cart);
    }

    @GetMapping("/user/{userId}")
    public CartResponse getCartByUserId(@PathVariable Long userId) {

        Cart cart = cartService.getCartByUserId(userId);
        return CartMapper.toCartResponse(cart);
    }

    @PostMapping("/user/{userId}/items")
    public CartResponse addItemToUserCart(@PathVariable Long userId, @RequestBody AddCartItemRequest request) {

        Cart cart = cartService.addProductToCartByUserId(userId, request.getVariantId(), request.getQuantity());
        return CartMapper.toCartResponse(cart);
    }

    @PutMapping("/user/{userId}/items/{variantId}")
    public CartResponse updateUserCartItemQuantity(@PathVariable Long userId, @PathVariable Long variantId, @RequestBody UpdateCartItemQuantityRequest request) {

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
