package com.example.shop.dto.user;

import com.example.shop.entity.Role;
import jakarta.validation.constraints.NotNull;

public record ChangeRoleRequest(
    @NotNull(message = "New role must not be null")
    Role newRole
){}
