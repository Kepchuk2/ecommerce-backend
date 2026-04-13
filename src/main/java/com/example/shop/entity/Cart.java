package com.example.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String sessionId;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    public Cart(String sessionId) {
        this.sessionId = sessionId;
    }

    public void addItem(CartItem item) {
        validateCartItem(item);

        if (items.contains(item)) {
            return;
        }
        if (item.getCart() != null && item.getCart() != this) {
            throw new IllegalArgumentException("Cart item already belongs to another cart");
        }

        items.add(item);
        item.setCart(this);
    }

    public void removeItem(CartItem item) {
        validateCartItem(item);

        if (items.remove(item)) {
            item.setCart(null);
        }
    }

    public void clearItems() {
        while (!items.isEmpty()) {
            CartItem item = items.get(0);
            removeItem(item);
        }
    }

    public Optional<CartItem> findItemByVariantId(Long variantId) {
        if (variantId == null) {
            return Optional.empty();
        }

        return items.stream()
                .filter(item -> item.getProductVariant() != null)
                .filter(item -> Objects.equals(item.getProductVariant().getId(), variantId))
                .findFirst();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void assignUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Cart user must not be null");
        }

        this.user = user;
        user.setCartIternal(this);
    }

    private void validateCartItem(CartItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Cart item must not be null");
        }
    }
}