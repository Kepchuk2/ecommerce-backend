package com.example.shop.mapper;

import com.example.shop.dto.cart.CartItemResponse;
import com.example.shop.dto.cart.CartResponse;
import com.example.shop.entity.Cart;
import com.example.shop.entity.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartMapper {

    public static CartItemResponse toCartItemResponse(CartItem item) {
        if (item == null) {
            return null;
        }
        CartItemResponse response = new CartItemResponse();

        response.setId(item.getId());
        response.setVariantId(item.getProductVariant() != null ? item.getProductVariant().getId() : null);
        response.setSku(item.getSku());
        response.setProductName(item.getProductName());
        response.setQuantity(item.getQuantity());
        response.setPrice(item.getPrice());
        response.setSize(item.getSize());
        response.setColor(item.getColor());
        response.setTotalPrice(item.getTotalPrice());

        return response;
    }

    public static List<CartItemResponse> toCartItemResponseList(List<CartItem> items) {
        List<CartItemResponse> responses = new ArrayList<>();

        if (items == null) {
            return responses;
        }

        for (CartItem item : items) {
            responses.add(toCartItemResponse(item));
        }

        return responses;
    }

    public static CartResponse toCartResponse(Cart cart) {
        if (cart == null) {
            return null;
        }

        CartResponse response = new CartResponse();

        response.setId(cart.getId());
        response.setSessionId(cart.getSessionId());
        response.setTotalPrice(cart.getTotalPrice());
        response.setItems(toCartItemResponseList(cart.getItems()));

        return response;
    }
}
