package com.example.shop.service;

import com.example.shop.entity.*;
import com.example.shop.exception.CartNotFoundException;
import com.example.shop.exception.UserNotFoundException;
import com.example.shop.exception.VariantNotFoundException;
import com.example.shop.repository.CartRepository;
import com.example.shop.repository.ProductVariantRepository;
import com.example.shop.repository.UserRepository;
import com.example.shop.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductVariantRepository productVariantRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void getCartBySessionId_shouldReturnCart_whenCartExists() {
        String sessionId = "fd47ec08-8381-44fa-9c2f-0c23f533b7d5";
        Cart cart = new Cart(sessionId);

        when(cartRepository.findBySessionIdWithItems(sessionId)).thenReturn(Optional.of(cart));

        Cart result = cartService.getCartBySessionId(sessionId);

        assertNotNull(result);
        assertEquals(sessionId, result.getSessionId());
        verify(cartRepository).findBySessionIdWithItems(sessionId);
    }

    @Test
    void getCartBySessionId_shouldThrow_whenCartNotFound() {
        String sessionId = "fd47ec08-8381-44fa-9c2f-0c23f533b7d5";

        when(cartRepository.findBySessionIdWithItems(sessionId)).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class,
                () -> cartService.getCartBySessionId(sessionId));

        verify(cartRepository).findBySessionIdWithItems(sessionId);
    }

    @Test
    void findOrCreateCartBySessionId_shouldReturnExistingCart_whenFound() {
        String sessionId = "fd47ec08-8381-44fa-9c2f-0c23f533b7d5";
        Cart existingCart = new Cart(sessionId);

        when(cartRepository.findBySessionIdWithItems(sessionId)).thenReturn(Optional.of(existingCart));

        Cart result = cartService.findOrCreateCartBySessionId(sessionId);

        assertSame(existingCart, result);
        verify(cartRepository).findBySessionIdWithItems(sessionId);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void findOrCreateCartBySessionId_shouldCreateCart_whenNotFound() {
        String sessionId = "fd47ec08-8381-44fa-9c2f-0c23f533b7d5";
        Cart savedCart = new Cart(sessionId);

        when(cartRepository.findBySessionIdWithItems(sessionId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(savedCart);

        Cart result = cartService.findOrCreateCartBySessionId(sessionId);

        assertNotNull(result);
        assertEquals(sessionId, result.getSessionId());

        verify(cartRepository).findBySessionIdWithItems(sessionId);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void findOrCreateCartByUserId_shouldCreateCartAndAssignUser_whenCartNotFound() {
        Long userId = 3L;
        User user = new User("test@example.com", "password123", Role.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        Cart savedCart = new Cart();

        when(cartRepository.findByUserIdWithItems(userId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.save(any(Cart.class))).thenReturn(savedCart);

        Cart result = cartService.findOrCreateCartByUserId(userId);

        assertNotNull(result);

        verify(cartRepository).findByUserIdWithItems(userId);
        verify(userRepository).findById(userId);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void findOrCreateCartByUserId_shouldThrow_whenUserNotFound() {
        Long userId = 3L;

        when(cartRepository.findByUserIdWithItems(userId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> cartService.findOrCreateCartByUserId(userId));

        verify(cartRepository).findByUserIdWithItems(userId);
        verify(userRepository).findById(userId);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void addProductToCartBySessionId_shouldAddNewItem_whenItemDoesNotExist() {
        String sessionId = "fd47ec08-8381-44fa-9c2f-0c23f533b7d5";
        Long variantId = 10L;
        int quantity = 2;

        Cart cart = new Cart(sessionId);
        ProductVariant variant = createVariant(variantId, "SKU-001", "Hoodie", new BigDecimal("99.99"));

        when(cartRepository.findBySessionIdWithItems(sessionId)).thenReturn(Optional.of(cart));
        when(productVariantRepository.findById(variantId)).thenReturn(Optional.of(variant));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart result = cartService.addProductToCartBySessionId(sessionId, variantId, quantity);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(quantity, result.getItems().get(0).getQuantity());
        assertEquals("SKU-001", result.getItems().get(0).getSku());

        verify(productVariantRepository).findById(variantId);
        verify(cartRepository).save(cart);
    }

    @Test
    void addProductToCartBySessionId_shouldIncreaseQuantity_whenItemAlreadyExists() {
        String sessionId = "fd47ec08-8381-44fa-9c2f-0c23f533b7d5";
        Long variantId = 10L;

        Cart cart = new Cart(sessionId);
        ProductVariant variant = createVariant(variantId, "SKU-001", "Hoodie", new BigDecimal("99.99"));
        cart.addItem(new CartItem(variant, 2));

        when(cartRepository.findBySessionIdWithItems(sessionId)).thenReturn(Optional.of(cart));
        when(productVariantRepository.findById(variantId)).thenReturn(Optional.of(variant));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart result = cartService.addProductToCartBySessionId(sessionId, variantId, 3);

        assertEquals(1, result.getItems().size());
        assertEquals(5, result.getItems().get(0).getQuantity());

        verify(cartRepository).save(cart);
    }

    @Test
    void addProductToCartBySessionId_shouldThrow_whenVariantNotFound() {
        String sessionId = "session-123";
        Long variantId = 10L;

        Cart cart = new Cart(sessionId);

        when(cartRepository.findBySessionIdWithItems(sessionId)).thenReturn(Optional.of(cart));
        when(productVariantRepository.findById(variantId)).thenReturn(Optional.empty());

        assertThrows(VariantNotFoundException.class,
                () -> cartService.addProductToCartBySessionId(sessionId, variantId, 2));

        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void updateCartItemQuantityBySessionId_shouldUpdateQuantity_whenItemExists() {
        String sessionId = "fd47ec08-8381-44fa-9c2f-0c23f533b7d5";
        Long variantId = 10L;

        Cart cart = new Cart(sessionId);
        ProductVariant variant = createVariant(variantId, "SKU-001", "Hoodie", new BigDecimal("99.99"));
        cart.addItem(new CartItem(variant, 2));

        when(cartRepository.findBySessionIdWithItems(sessionId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart result = cartService.updateCartItemQuantityBySessionId(sessionId, variantId, 7);

        assertEquals(1, result.getItems().size());
        assertEquals(7, result.getItems().get(0).getQuantity());

        verify(cartRepository).save(cart);
    }

    @Test
    void updateCartItemQuantityBySessionId_shouldRemoveItem_whenNewQuantityIsZero() {
        String sessionId = "fd47ec08-8381-44fa-9c2f-0c23f533b7d5";
        Long variantId = 10L;

        Cart cart = new Cart(sessionId);
        ProductVariant variant = createVariant(variantId, "SKU-001", "Hoodie", new BigDecimal("99.99"));
        cart.addItem(new CartItem(variant, 2));

        when(cartRepository.findBySessionIdWithItems(sessionId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart result = cartService.updateCartItemQuantityBySessionId(sessionId, variantId, 0);

        assertTrue(result.getItems().isEmpty());

        verify(cartRepository).save(cart);
    }

    @Test
    void clearCartBySessionId_shouldRemoveAllItems() {
        String sessionId = "fd47ec08-8381-44fa-9c2f-0c23f533b7d5";

        Cart cart = new Cart(sessionId);
        ProductVariant variant1 = createVariant(10L, "SKU-001", "Hoodie", new BigDecimal("99.99"));
        ProductVariant variant2 = createVariant(11L, "SKU-002", "Sneakers", new BigDecimal("149.99"));

        cart.addItem(new CartItem(variant1, 2));
        cart.addItem(new CartItem(variant2, 1));

        when(cartRepository.findBySessionIdWithItems(sessionId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart result = cartService.clearCartBySessionId(sessionId);

        assertNotNull(result);
        assertTrue(result.getItems().isEmpty());

        verify(cartRepository).save(cart);
    }

    @Test
    void addProductToCartBySessionId_shouldThrow_whenQuantityInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> cartService.addProductToCartBySessionId("fd47ec08-8381-44fa-9c2f-0c23f533b7d5", 10L, 0));

        verifyNoInteractions(cartRepository, productVariantRepository, userRepository);
    }

    private ProductVariant createVariant(Long variantId, String sku, String productName, BigDecimal price) {
        Product product = new Product(productName, "test description", ProductCategory.CLOTHING);
        ProductVariant variant = new ProductVariant(sku, price, "M", "Black");
        product.addVariant(variant);

        ReflectionTestUtils.setField(variant, "id", variantId);

        return variant;
    }
}
