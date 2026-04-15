package com.example.shop.repository;

import com.example.shop.entity.Order;
import com.example.shop.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByUserId(Long userId);

    @Query("""
       select distinct o from Order o
       left join fetch o.items
       left join fetch o.user
       where o.id = :orderId
       """)
    Optional<Order> findByIdWithDetails(Long orderId);

    @Query("""
       select distinct o from Order o
       left join fetch o.items
       left join fetch o.user
       where o.user.id = :userId
       """)
    List<Order> findByUserIdWithDetails(Long userId);
}
