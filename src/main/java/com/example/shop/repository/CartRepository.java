package com.example.shop.repository;

import com.example.shop.entity.Cart;
import com.example.shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    //Найти корзину по session_id
    Optional<Cart> findBySessionId(String sessionId);

    Optional<Cart> findByUserId(Long userId);

    @Query("""
           select distinct c from Cart c
           left join fetch c.items i
           left join fetch i.productVariant
           where c.sessionId = :sessionId
           """)
    Optional<Cart> findBySessionIdWithItems(String sessionId);

    @Query("""
           select distinct c from Cart c
           left join fetch c.items i
           left join fetch i.productVariant
           where c.user.id = :userId
           """)
    Optional<Cart> findByUserIdWithItems(Long userId);
}

