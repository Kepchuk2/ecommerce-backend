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

@Entity
@Table(name = "orders")
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_seq")
    @SequenceGenerator(name = "orders_seq", sequenceName = "orders_seq", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    private String deliveryAddress;
    private String deliveryMethod;
    private String trackingNumber;

    @CreationTimestamp
    @Column(nullable = false ,updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Order() {
        this.status = OrderStatus.NEW;
        this.currency = Currency.UAH;
        this.totalPrice = BigDecimal.ZERO;
    }

    public Order(OrderStatus status, BigDecimal totalPrice, Currency currency, String deliveryAddress, String deliveryMethod) {
        validateOrderStatus(status);
        if (totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total price must be greater than or equal to zero");
        }
        if (currency == null) {
            throw new IllegalArgumentException("Currency must not be null");
        }

        this.status = status;
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.deliveryAddress = deliveryAddress;
        this.deliveryMethod = deliveryMethod;
    }

    public void changeStatus(OrderStatus newStatus) {
        validateOrderStatus(newStatus);

        this.status = newStatus;
    }

    public void assignTrackingNumber(String trackingNumber) {
        if (trackingNumber == null || trackingNumber.isBlank()) {
            throw new IllegalArgumentException("Tracking number must not be blank");
        }
        this.trackingNumber = trackingNumber;
    }

    public void addItem(OrderItem item) {
        validateOrderItem(item);
        if (item.getOrder() != null && item.getOrder() != this) {
            throw new IllegalArgumentException("Order item already belongs to another order");
        }
        if (items.contains(item)) {
            return;
        }

        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItem item) {
        validateOrderItem(item);

        items.remove(item);
        item.setOrder(null);
    }

    public BigDecimal calculateTotal() {
        return items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void refreshTotalPrice() {
        this.totalPrice = calculateTotal();
    }

    public void assignUser(User user) {
        this.user = user;
    }

    private void validateOrderItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Order item must not be null");
        }
    }

    private void validateOrderStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Order status must not be null");
        }
    }
}