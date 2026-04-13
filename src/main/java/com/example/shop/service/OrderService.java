package com.example.shop.service;

import com.example.shop.entity.*;
import com.example.shop.exception.CartNotFoundException;
import com.example.shop.exception.OrderNotFoundException;
import com.example.shop.exception.UserNotFoundException;
import com.example.shop.repository.CartRepository;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public Order getOrderById(Long orderId) {
        validateOrderId(orderId);

        return orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public List<Order> getOrdersByUserId(Long userId) {
        validateUserId(userId);

        return orderRepository.findByUserIdWithDetails(userId);
    }

    @Transactional
    public Order createOrderFromUserCart(Long userId) {
        validateUserId(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        return createOrderFromCart(cart, user);
    }

    @Transactional
    public Order createOrderFromSessionCart(String sessionId) {
        validateSessionId(sessionId);

        Cart cart = cartRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new CartNotFoundException(sessionId));

        return createOrderFromCart(cart, null);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        validateOrderId(orderId);

        if (orderStatus == null) {
            throw new IllegalArgumentException("Order status cannot be null");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.changeStatus(orderStatus);

        Order savedOrder = orderRepository.save(order);

        return orderRepository.findByIdWithDetails(savedOrder.getId())
                .orElseThrow(() -> new OrderNotFoundException(savedOrder.getId()));
    }

    @Transactional
    public Order cancelOrder(Long orderId) {
        validateOrderId(orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.changeStatus(OrderStatus.CANCELLED);

        Order savedOrder = orderRepository.save(order);

        return orderRepository.findByIdWithDetails(savedOrder.getId())
                .orElseThrow(() -> new OrderNotFoundException(savedOrder.getId()));
    }

    private Order createOrderFromCart(Cart cart, User user) {
        validateCartNotEmpty(cart);

        Order order = new Order();

        if (user != null) {
            order.assignUser(user);
        }

        addCartItemsToOrder(cart, order);
        order.refreshTotalPrice();

        Order savedOrder = orderRepository.save(order);
        cart.clearItems();

        return orderRepository.findByIdWithDetails(savedOrder.getId())
                .orElseThrow(() -> new OrderNotFoundException(savedOrder.getId()));
    }

    private void validateOrderId(Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("orderId must not be null and must be greater than zero.");
        }
    }

    private void validateUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId must not be null and must be greater than zero.");
        }
    }

    private void validateSessionId(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException("Session id cannot be null or blank");
        }
    }

    private void validateCartNotEmpty(Cart cart) {
        if (cart.isEmpty()) {
            throw new IllegalArgumentException("cart must not be empty.");
        }
    }

    private void addCartItemsToOrder(Cart cart, Order order) {
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem(cartItem);
            order.addItem(orderItem);
        }
    }
}
