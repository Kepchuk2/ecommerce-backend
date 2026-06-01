package com.example.shop.controller;

import com.example.shop.dto.order.CreateOrderRequest;
import com.example.shop.dto.order.OrderResponse;
import com.example.shop.dto.order.UpdateOrderStatusRequest;
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
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponse> getOrdersByUserId(@PathVariable Long userId) {
        return orderService.getOrdersByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        if (request.userId() != null) {
            return orderService.createOrderFromUserCart(request.userId());
        } else if (request.sessionId() != null) {
            return orderService.createOrderFromSessionCart(request.sessionId());
        } else {
            throw new IllegalArgumentException("Either userId or sessionId must be provided to create an order");
        }
    }

    @PatchMapping("/{orderId}/status")
    public OrderResponse updateOrderStatus(@PathVariable Long orderId, @Valid @RequestBody UpdateOrderStatusRequest request) {
        return orderService.updateOrderStatus(orderId, request.status());
    }

    @PatchMapping("/{orderId}/cancel")
    public OrderResponse cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }
}
