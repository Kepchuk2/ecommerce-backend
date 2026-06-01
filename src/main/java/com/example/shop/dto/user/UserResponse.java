package com.example.shop.dto.user;

import com.example.shop.entity.Role;

public record UserResponse( 
    Long id,
    String email,
    Role role
){}
