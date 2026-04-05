package com.example.shop.repository;

import com.example.shop.entity.Cart;
import com.example.shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    //Найти корзину по session_id
    Optional<Cart> findBySessionId(String sessionId);

    Optional<Cart> findByUser(User user);

    Optional<Cart> findByUserId(Long userId);
}
