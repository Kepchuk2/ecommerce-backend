package com.example.shop;

import com.example.shop.entity.*;
import com.example.shop.exception.OrderNotFoundException;
import com.example.shop.repository.CartRepository;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.UserRepository;
import com.example.shop.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrderFromUserCart_shouldCreateOrderAndClearCart() throws Exception {
        Long userId = 1L;
        Long variantId = 10L;

        User user = new User("test@example.com", "password123", Role.USER);

        Product product = new Product("Hoodie", "Warm hoodie", ProductCategory.CLOTHING);
        ProductVariant variant = new ProductVariant("SKU-1", new BigDecimal("1200.00"), "L", "Black");
        product.addVariant(variant);
        setId(variant, variantId);

        Cart cart = new Cart();
        cart.assignUser(user);
        cart.addItem(new CartItem(variant, 2));

        Order savedOrder = new Order();
        setId(savedOrder, 100L);

        Order detailedOrder = new Order();
        setId(detailedOrder, 100L);
        detailedOrder.addItem(new OrderItem("SKU-1", variantId, new BigDecimal("1200.00"), "Hoodie", 2, "L", "Black"));
        detailedOrder.refreshTotalPrice();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderRepository.findByIdWithDetails(100L)).thenReturn(Optional.of(detailedOrder));

        Order result = orderService.createOrderFromUserCart(userId);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(new BigDecimal("2400.00"), result.getTotalPrice());
        assertTrue(cart.getItems().isEmpty());

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void updateOrderStatus_shouldChangeStatus() throws Exception {
        Order order = new Order();
        setId(order, 1L);

        Order updatedOrder = new Order();
        setId(updatedOrder, 1L);
        updatedOrder.changeStatus(OrderStatus.SHIPPED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);
        when(orderRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(updatedOrder));

        Order result = orderService.updateOrderStatus(1L, OrderStatus.SHIPPED);

        assertEquals(OrderStatus.SHIPPED, result.getStatus());
    }

    @Test
    void updateOrderStatus_shouldThrowWhenOrderNotFoundAfterSave() throws Exception {
        Order order = new Order();
        setId(order, 1L);

        Order savedOrder = new Order();
        setId(savedOrder, 1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderRepository.findByIdWithDetails(anyLong())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class,
                () -> orderService.updateOrderStatus(1L, OrderStatus.SHIPPED));
    }

    private void setId(Object target, Long idValue) throws Exception {
        Field field = target.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(target, idValue);
    }
}
