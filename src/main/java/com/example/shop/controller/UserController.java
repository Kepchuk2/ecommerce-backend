package com.example.shop.controller;

import com.example.shop.dto.user.ChangePasswordRequest;
import com.example.shop.dto.user.ChangeRoleRequest;
import com.example.shop.dto.user.CreateUserRequest;
import com.example.shop.dto.user.UserResponse;
import com.example.shop.entity.Role;
import com.example.shop.entity.User;
import com.example.shop.mapper.UserMapper;
import com.example.shop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public UserResponse getUserById(@PathVariable Long userId) {
        return UserMapper.toUserResponse(userService.getUserById(userId));
    }

    @GetMapping("/email")
    public UserResponse getUserByEmail(@RequestParam String email) {
        return UserMapper.toUserResponse(userService.getUserByEmail(email));
    }

    @GetMapping
    public List<UserResponse> getUsersByRole(@RequestParam Role role) {
        return UserMapper.toUserResponseList(userService.getUsersByRole(role));
    }

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = userService.createUser(request.getEmail(), request.getPassword(), request.getRole());
        return UserMapper.toUserResponse(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
    }

    @PatchMapping("/{userId}/password")
    public UserResponse changePassword(@PathVariable Long userId, @Valid @RequestBody ChangePasswordRequest request) {
        return UserMapper.toUserResponse(userService.changePassword(userId, request.getNewPassword()));
    }

    @PatchMapping("/{userId}/role")
    public UserResponse changeRole(@PathVariable Long userId, @Valid @RequestBody ChangeRoleRequest request) {
        return UserMapper.toUserResponse(userService.changeRole(userId, request.getNewRole()));
    }
}
