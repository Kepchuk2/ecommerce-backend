package com.example.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_items",
        indexes = {
        @Index(name = "idx_cart_items_cart", columnList = "cart_id")
},
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"cart_id", "variant_id"})
        })
@Getter
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;

    private String size;
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant productVariant;

    public CartItem(ProductVariant variant, int quantity) {
        validateVariant(variant);
        validateQuantity(quantity);

        this.productVariant = variant;
        this.sku = variant.getSku();
        this.productName = variant.getProduct().getName();
        this.price = variant.getPrice();
        this.size = variant.getSize();
        this.color = variant.getColor();
        this.quantity = quantity;
    }

    void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setQuantity(int quantity) {
        validateQuantity(quantity);

        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }

    private void validateVariant(ProductVariant variant) {
        if (variant == null) {
            throw new IllegalArgumentException("Product variant must not be null");
        }
    }
}
