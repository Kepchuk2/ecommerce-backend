package com.example.shop.mapper;

import com.example.shop.dto.order.OrderItemResponse;
import com.example.shop.dto.order.OrderResponse;
import com.example.shop.entity.Order;
import com.example.shop.entity.OrderItem;

import java.util.ArrayList;
import java.util.List;

public final class OrderMapper {

    private static OrderItemResponse toItemResponse(OrderItem item) {
        if (item == null) {
            return null;
        }

        OrderItemResponse itemResponse = new OrderItemResponse();

        itemResponse.setId(item.getId());
        itemResponse.setSku(item.getSku());
        itemResponse.setVariantId(item.getVariantId());
        itemResponse.setPrice(item.getPrice());
        itemResponse.setProductName(item.getProductName());
        itemResponse.setQuantity(item.getQuantity());
        itemResponse.setSize(item.getSize());
        itemResponse.setColor(item.getColor());
        itemResponse.setTotalPrice(item.getTotalPrice());

        return itemResponse;

    }

    private static List<OrderItemResponse> toOrderItemResponseList(List<OrderItem> items) {
        List<OrderItemResponse> responses = new ArrayList<>();

        if (items == null) {
            return responses;
        }

        for (OrderItem item : items) {
            if (item != null) {
                responses.add(toItemResponse(item));
            }
        }

        return responses;
    }


    public static OrderResponse toOrderResponse(Order order) {
        if (order == null) {
            return null;
        }

        Long userId = order.getUser() != null ? order.getUser().getId() : null;

        OrderResponse response = new OrderResponse();

        response.setId(order.getId());
        response.setUserId(userId);
        response.setStatus(order.getStatus());
        response.setTotalPrice(order.getTotalPrice());
        response.setCurrency(order.getCurrency());
        response.setDeliveryAddress(order.getDeliveryAddress());
        response.setDeliveryMethod(order.getDeliveryMethod());
        response.setTrackingNumber(order.getTrackingNumber());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        response.setItems(toOrderItemResponseList(order.getItems()));

        return  response;
    }

    public static List<OrderResponse> toOrderResponseList(List<Order> orders) {
        List<OrderResponse> responses = new ArrayList<>();

        if (orders == null) {
            return responses;
        }

        for (Order order : orders) {
            if (order != null) {
            responses.add(toOrderResponse(order));
            }
        }
        return responses;
    }
}
