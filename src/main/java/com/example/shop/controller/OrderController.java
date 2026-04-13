package com.example.shop.controller;

import com.example.shop.dto.order.OrderResponse;
import com.example.shop.dto.order.UpdateOrderStatusRequest;
import com.example.shop.entity.Order;
import com.example.shop.mapper.OrderMapper;
import com.example.shop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public OrderResponse getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return OrderMapper.toOrderResponse(order);
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponse> getOrdersByUserId(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return OrderMapper.toOrderResponseList(orders);
    }

    @PostMapping("/from-user-cart/{userId}")
    public OrderResponse createOrderFromUserCart(@PathVariable Long userId) {
        Order order = orderService.createOrderFromUserCart(userId);
        return OrderMapper.toOrderResponse(order);
    }

    @PostMapping("/from-session-cart/{sessionId}")
    public OrderResponse createOrderFromSessionCart(@PathVariable String sessionId) {
        Order order = orderService.createOrderFromSessionCart(sessionId);
        return OrderMapper.toOrderResponse(order);
    }

    @PatchMapping("/{orderId}/status")
    public OrderResponse updateOrderStatus(@PathVariable Long orderId, @Valid @RequestBody UpdateOrderStatusRequest request) {
        Order order = orderService.updateOrderStatus(orderId, request.getStatus());
        return OrderMapper.toOrderResponse(order);
    }

    @PatchMapping("/{orderId}/cancel")
    public OrderResponse cancelOrder(@PathVariable Long orderId) {
        Order order = orderService.cancelOrder(orderId);
        return OrderMapper.toOrderResponse(order);
    }
}
