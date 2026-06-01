package com.example.shop.service;

import com.example.shop.dto.user.UserResponse;
import com.example.shop.entity.Role;
import com.example.shop.entity.User;
import com.example.shop.exception.UserAlreadyExistsException;
import com.example.shop.exception.UserDeletionException;
import com.example.shop.exception.UserNotFoundException;
import com.example.shop.mapper.UserMapper;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final UserMapper userMapper;

    public UserResponse getUserById(Long userId) {
        validateUserId(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return userMapper.toUserResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        validateEmail(email);

        String normalizedEmail = normalizeEmail(email);

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new UserNotFoundException(normalizedEmail));

        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> getUsersByRole(Role role) {
        validateRole(role);

        List<User> user = userRepository.findByRole(role);

        return userMapper.toUserResponseList(user);
    }

    @Transactional
    public UserResponse createUser(String email, String password, Role role) {
        validateEmail(email);
        validatePassword(password);
        validateRole(role);

        String normalizedEmail = normalizeEmail(email);

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new UserAlreadyExistsException(normalizedEmail);
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(normalizedEmail, encodedPassword, role);
        User savedUser = userRepository.save(user);

        return userMapper.toUserResponse(savedUser);
    }

    @Transactional
    public void deleteUserById(Long userId) {
        validateUserId(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (orderRepository.existsByUserId(userId)) {
            throw new UserDeletionException("Cannot delete user with existing orders");
        }

        userRepository.delete(user);
    }

    @Transactional
    public UserResponse changePassword(Long userId, String newPassword) {
        validateUserId(userId);
        validateNewPassword(newPassword);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.changePassword(encodedPassword);

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse changeRole(Long userId, Role role) {
        validateUserId(userId);
        validateRole(role);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.changeRole(role);
        return userMapper.toUserResponse(user);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email cannot be null or blank");
        }
    }

    private void validateRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("role cannot be null");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password cannot be null or blank");
        }
    }

    private void validateNewPassword(String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("newPassword cannot be null or blank");
        }
    }
}
