package com.example.shop.repository;

import com.example.shop.entity.Cart;
import com.example.shop.entity.CartItem;
import com.example.shop.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    //Получить все товары в конкретной корзине
    List<CartItem> findByCart(Cart cart);

    List<CartItem> findByCartId(Long cartId);

    Optional<CartItem> findByCartIdAndProductVariantId(Cart cartId, ProductVariant variantId);

    // Удалить все товары из корзины
    void deleteByCart(Cart cart);

    void deleteByCartAndProductVariant(Cart cart, ProductVariant variant);
}