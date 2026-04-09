package com.example.shop.service;

import com.example.shop.entity.Role;
import com.example.shop.entity.User;
import com.example.shop.exception.UserAlreadyExistsException;
import com.example.shop.exception.UserNotFoundException;
import com.example.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long userId) {
        validateUserId(userId);

        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public User getUserByEmail(String email) {
        validateEmail(email);

        String normalizedEmail = normalizeEmail(email);

        return userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new UserNotFoundException(normalizedEmail));
    }

    public List<User> getUsersByRole(Role role) {
        validateRole(role);

        return userRepository.findByRole(role);
    }

    public User createUser(String email, String password, Role role) {
        validateEmail(email);
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password cannot be null or blank");
        }
        validateRole(role);

        String normalizedEmail = normalizeEmail(email);

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new UserAlreadyExistsException(normalizedEmail);
        }

        User user = new User(normalizedEmail, password, role);
        return userRepository.save(user);
    }

    public void deleteUserById(Long userId) {
        validateUserId(userId);

        User user = getUserById(userId);

        if(!user.getOrders().isEmpty()) {
            throw new IllegalStateException("Cannot delete user with existing orders");
        }

        userRepository.delete(user);
    }

    public User changePassword(Long userId, String newPassword) {
        validateUserId(userId);
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("newPassword cannot be null or blank");
        }
        User user = getUserById(userId);

        user.changePassword(newPassword);
        return userRepository.save(user);
    }

    public User changeRole(Long userId, Role role) {
        validateUserId(userId);
        validateRole(role);

        User user = getUserById(userId);

        user.changeRole(role);
        return userRepository.save(user);
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
}
