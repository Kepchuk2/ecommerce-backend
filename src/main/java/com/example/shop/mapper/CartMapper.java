package com.example.shop.mapper;

import com.example.shop.dto.cart.CartItemResponse;
import com.example.shop.dto.cart.CartResponse;
import com.example.shop.entity.Cart;
import com.example.shop.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    CartResponse toCartResponse(Cart cart);

    @Mapping(target = "variantId", source = "productVariant.id")
    CartItemResponse toCartItemResponse(CartItem item);

    List<CartItemResponse> toCartItemResponseList(List<CartItem> items);
}
