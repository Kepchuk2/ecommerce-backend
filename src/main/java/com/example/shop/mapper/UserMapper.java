package com.example.shop.mapper;

import com.example.shop.dto.user.UserResponse;
import com.example.shop.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> users);

}
