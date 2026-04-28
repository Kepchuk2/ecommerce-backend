package com.example.shop.controller;

import com.example.shop.dto.order.CreateOrderRequest;
import com.example.shop.dto.order.OrderResponse;
import com.example.shop.dto.order.UpdateOrderStatusRequest;
import com.example.shop.entity.Order;
import com.example.shop.mapper.OrderMapper;
import com.example.shop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Order order;

        if (request.getUserId() != null) {
            order = orderService.createOrderFromUserCart(request.getUserId());
        } else if (request.getSessionId() != null) {
            order = orderService.createOrderFromSessionCart(request.getSessionId());
        } else {
            throw new IllegalArgumentException("Either userId or sessionId must be provided to create an order");
        }

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
