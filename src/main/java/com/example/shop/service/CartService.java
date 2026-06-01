package com.example.shop.service;

import com.example.shop.dto.cart.CartResponse;
import com.example.shop.entity.*;
import com.example.shop.exception.CartNotFoundException;
import com.example.shop.exception.UserNotFoundException;
import com.example.shop.exception.VariantNotFoundException;
import com.example.shop.mapper.CartMapper;
import com.example.shop.repository.CartRepository;
import com.example.shop.repository.ProductVariantRepository;
import com.example.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductVariantRepository productVariantRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    public CartResponse getCartBySessionId(String sessionId) {
        validateSessionId(sessionId);

        Cart cart = cartRepository.findBySessionIdWithItems(sessionId)
                .orElseThrow(() -> new CartNotFoundException(sessionId));

        return cartMapper.toCartResponse(cart);
    }

    public CartResponse getCartByUserId(Long userId) {
        validateUserId(userId);

        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        return cartMapper.toCartResponse(cart);
    }

    @Transactional
    public CartResponse findOrCreateCartBySessionId(String sessionId) {
        validateSessionId(sessionId);

        Cart cart = cartRepository.findBySessionIdWithItems(sessionId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setSessionId(sessionId);
                    return cartRepository.save(newCart);
                });

        return cartMapper.toCartResponse(cart);
    }

    @Transactional
    public CartResponse findOrCreateCartByUserId(Long userId) {
        validateUserId(userId);

        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new UserNotFoundException(userId));

                    Cart newCart = new Cart();
                    newCart.assignUser(user);

                    return cartRepository.save(newCart);
                });

        return cartMapper.toCartResponse(cart);
    }

    private void addProductToCart(Cart cart, ProductVariant variant, int quantity) {
        validateCart(cart);
        validateProductVariant(variant);
        validateQuantity(quantity);

        Optional<CartItem> existingItem = cart.findItemByVariantId(variant.getId());

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else  {
            CartItem item = new CartItem(variant, quantity);
            cart.addItem(item);
        }
    }

    @Transactional
    public CartResponse addProductToCartBySessionId(String sessionId, Long variantId, int quantity) {
        validateSessionId(sessionId);
        validateVariantId(variantId);
        validateQuantity(quantity);

        Cart cart = cartRepository.findBySessionIdWithItems(sessionId)
                .orElseThrow(() -> new CartNotFoundException(sessionId));

        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        addProductToCart(cart, variant, quantity);

        return cartMapper.toCartResponse(cart);
    }

    @Transactional
    public CartResponse addProductToCartByUserId(Long userId, Long variantId, int quantity) {
        validateUserId(userId);
        validateVariantId(variantId);
        validateQuantity(quantity);

        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        addProductToCart(cart, variant, quantity);

        return cartMapper.toCartResponse(cart);
    }

    private void updateCartItemQuantity(Cart cart, Long variantId, int newQuantity) {
        validateCart(cart);
        validateVariantId(variantId);
        validateNewQuantity(newQuantity);

        CartItem item = cart.findItemByVariantId(variantId)
                .orElseThrow(() -> new IllegalArgumentException("cart item not found for variantId: " + variantId));

        if (newQuantity == 0) {
            cart.removeItem(item);
        } else {
            item.setQuantity(newQuantity);
        }
    }

    @Transactional
    public CartResponse updateCartItemQuantityBySessionId(String sessionId, Long variantId, int newQuantity) {
        validateSessionId(sessionId);
        validateVariantId(variantId);

        Cart cart = cartRepository.findBySessionIdWithItems(sessionId)
                .orElseThrow(() -> new CartNotFoundException(sessionId));

        updateCartItemQuantity(cart, variantId, newQuantity);

        return cartMapper.toCartResponse(cart);
    }

    @Transactional
    public CartResponse updateCartItemQuantityByUserId(Long userId, Long variantId, int newQuantity) {
        validateUserId(userId);
        validateVariantId(variantId);

        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        updateCartItemQuantity(cart, variantId, newQuantity);

        return cartMapper.toCartResponse(cart);
    }

    private void removeItemFromCart(Cart cart, Long variantId) {
        validateCart(cart);
        validateVariantId(variantId);

        cart.findItemByVariantId(variantId)
                .ifPresent(cart::removeItem);
    }

    @Transactional
    public CartResponse removeItemFromCartBySessionId(String sessionId, Long variantId) {
        validateSessionId(sessionId);
        validateVariantId(variantId);

        Cart cart = cartRepository.findBySessionIdWithItems(sessionId)
                .orElseThrow(() -> new CartNotFoundException(sessionId));

        removeItemFromCart(cart, variantId);

        return cartMapper.toCartResponse(cart);
    }

    @Transactional
    public CartResponse removeItemFromCartByUserId(Long userId, Long variantId) {
        validateUserId(userId);
        validateVariantId(variantId);

        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        removeItemFromCart(cart, variantId);

        return cartMapper.toCartResponse(cart);
    }

    private void clearCart(Cart cart) {
        validateCart(cart);
        cart.clearItems();
    }

    @Transactional
    public CartResponse clearCartBySessionId(String sessionId) {
        validateSessionId(sessionId);

        Cart cart = cartRepository.findBySessionIdWithItems(sessionId)
                .orElseThrow(() -> new CartNotFoundException(sessionId));

        clearCart(cart);

        return cartMapper.toCartResponse(cart);
    }

    @Transactional
    public CartResponse clearCartByUserId(Long userId) {
        validateUserId(userId);

        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        clearCart(cart);

        return cartMapper.toCartResponse(cart);
    }

    private void validateSessionId(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException("sessionId must not be null or blank");
        }
    }

    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId must not be null");
        }
    }

    private void validateVariantId(Long variantId) {
        if (variantId == null || variantId <= 0) {
            throw new IllegalArgumentException("variantId must not be null and must be greater than zero");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be zero or greater");
        }
    }

    private void validateCart(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("cart must not be null");
        }
    }

    private void validateProductVariant(ProductVariant variant) {
        if (variant == null) {
            throw new IllegalArgumentException("variant must not be null");
        }
    }

    private void validateNewQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be greater than zero");
        }
    }
}