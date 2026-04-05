package com.example.shop.repository;

import com.example.shop.entity.Order;
import com.example.shop.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Поиск заказа по статусу
    List<Order> findByStatus(OrderStatus status);

    // Получить все заказы по дате создания(сначала новые)
    List<Order> findAllByOrderByCreatedAtDesc();

    List<Order> findByUserId(Long userId);

    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);
}
