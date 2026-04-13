package com.example.shop.dto.user;

import com.example.shop.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    String email;

    @NotBlank(message = "Password must not be blank")
    String password;

    @NotNull(message = "Role must not be null")
    Role role;
}
