package com.example.shop;

import com.example.shop.entity.Role;
import com.example.shop.entity.User;
import com.example.shop.exception.UserAlreadyExistsException;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.UserRepository;
import com.example.shop.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_shouldNormalizeEmailAndSaveUser() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");

        User savedUser = new User("test@example.com", "encodedPassword123", Role.USER);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createUser("  TEST@EXAMPLE.COM  ", "password123", Role.USER);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();

        assertEquals("test@example.com", capturedUser.getEmail());
        assertEquals("encodedPassword123", capturedUser.getPassword());
        assertEquals(Role.USER, capturedUser.getRole());

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("encodedPassword123", result.getPassword());

        verify(passwordEncoder).encode("password123");
    }

    @Test
    void createUser_shouldThrowExceptionWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.createUser("test@example.com", "password123", Role.USER)
        );

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void changePassword_shouldUpdatePasswordAndSaveUser() {
        User existingUser = new User("test@example.com", "oldEncodedPassword", Role.USER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPassword123")).thenReturn("newEncodedPassword123");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userService.changePassword(1L, "newPassword123");

        assertNotNull(updatedUser);
        assertEquals("newEncodedPassword123", updatedUser.getPassword());

        verify(passwordEncoder).encode("newPassword123");
        verify(userRepository).save(existingUser);
    }

    @Test
    void deleteUserById_shouldThrowExceptionWhenUserHasOrders() {
        User existingUser = new User("test@example.com", "encodedPassword123", Role.USER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(orderRepository.existsByUserId(1L)).thenReturn(true);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> userService.deleteUserById(1L)
        );

        assertEquals("Cannot delete user with existing orders", exception.getMessage());
        verify(userRepository, never()).delete(any(User.class));
    }
}
