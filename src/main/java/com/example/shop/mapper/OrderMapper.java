package com.example.shop.mapper;

import com.example.shop.dto.order.OrderItemResponse;
import com.example.shop.dto.order.OrderResponse;
import com.example.shop.entity.Order;
import com.example.shop.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "userId", source = "user.id")
    OrderResponse toOrderResponse(Order order);

    OrderItemResponse toItemResponse(OrderItem item);

    List<OrderItemResponse> toOrderItemResponseList(List<OrderItem> items);

    List<OrderResponse> toOrderResponseList(List<Order> orders);

}
