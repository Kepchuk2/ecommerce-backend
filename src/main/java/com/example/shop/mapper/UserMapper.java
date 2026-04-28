package com.example.shop.mapper;

import com.example.shop.dto.user.UserResponse;
import com.example.shop.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    private UserMapper() {}

    public static UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return  response;
    }

    public static List<UserResponse> toUserResponseList(List<User> users) {
        List<UserResponse> responses = new ArrayList<>();

        if (users == null) {
            return responses;
        }

        for (User user : users) {
            responses.add(toUserResponse(user));
        }

        return responses;
    }
}
