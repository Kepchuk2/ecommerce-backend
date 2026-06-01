package com.example.shop.service;

import com.example.shop.dto.order.OrderResponse;
import com.example.shop.entity.*;
import com.example.shop.exception.CartNotFoundException;
import com.example.shop.exception.OrderNotFoundException;
import com.example.shop.exception.UserNotFoundException;
import com.example.shop.mapper.OrderMapper;
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
    private final OrderMapper orderMapper;

    public OrderResponse getOrderById(Long orderId) {
        validateOrderId(orderId);

        Order order = orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        return orderMapper.toOrderResponse(order);
    }

    public List<OrderResponse> getOrdersByUserId(Long userId) {
        validateUserId(userId);

        List<Order> order = orderRepository.findByUserIdWithDetails(userId);

        return orderMapper.toOrderResponseList(order);
    }

    @Transactional
    public OrderResponse createOrderFromUserCart(Long userId) {
        validateUserId(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        return createOrderFromCart(cart, user);
    }

    @Transactional
    public OrderResponse createOrderFromSessionCart(String sessionId) {
        validateSessionId(sessionId);

        Cart cart = cartRepository.findBySessionIdWithItems(sessionId)
                .orElseThrow(() -> new CartNotFoundException(sessionId));

        return createOrderFromCart(cart, null);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        validateOrderId(orderId);
        validateOrderStatus(orderStatus);

        Order order = orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.changeStatus(orderStatus);

        return orderMapper.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId) {
        validateOrderId(orderId);

        Order order = orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.changeStatus(OrderStatus.CANCELLED);

        return orderMapper.toOrderResponse(order);
    }

    private OrderResponse createOrderFromCart(Cart cart, User user) {
        validateCartNotEmpty(cart);

        Order order = new Order();

        if (user != null) {
            user.addOrder(order);
        }

        addCartItemsToOrder(cart, order);
        order.refreshTotalPrice();

        Order savedOrder = orderRepository.save(order);
        cart.clearItems();

        Order fullOrder = orderRepository.findByIdWithDetails(savedOrder.getId())
                .orElseThrow(() -> new OrderNotFoundException(savedOrder.getId()));

        return orderMapper.toOrderResponse(fullOrder);
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

    private void validateOrderStatus(OrderStatus orderStatus) {
        if (orderStatus == null) {
            throw new IllegalArgumentException("Order status must not be null.");
        }
    }
}
