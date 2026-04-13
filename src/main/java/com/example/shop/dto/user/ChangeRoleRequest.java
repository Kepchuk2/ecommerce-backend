package com.example.shop.dto.user;

import com.example.shop.entity.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeRoleRequest {

    @NotNull(message = "New role must not be null")
    Role newRole;
}
