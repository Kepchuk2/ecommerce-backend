package com.example.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sku;

    private Long variantId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    private String size;
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public OrderItem(String sku, Long variantId, BigDecimal price, String productName, int quantity, String size, String color) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU must not be blank");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be greater than or equal to zero");
        }
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name must not be blank");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        this.sku = sku;
        this.variantId = variantId;
        this.price = price;
        this.productName = productName;
        this.quantity = quantity;
        this.size = size;
        this.color = color;
    }

    public OrderItem(CartItem cartItem) {
        if (cartItem == null) {
            throw new IllegalArgumentException("Cart item must not be null");
        }

        this.sku = cartItem.getSku();
        this.variantId = cartItem.getProductVariant() != null ? cartItem.getProductVariant().getId() : null;
        this.price = cartItem.getPrice();
        this.productName = cartItem.getProductName();
        this.quantity = cartItem.getQuantity();
        this.size = cartItem.getSize();
        this.color = cartItem.getColor();
    }

    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}