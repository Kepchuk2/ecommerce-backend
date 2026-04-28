package com.example.shop.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "product_variants",
        indexes = {
        @Index(name = "idx_product_variants_product", columnList = "product_id")
})
@Getter
@NoArgsConstructor
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    private String size;
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public ProductVariant(String sku, BigDecimal price, String size, String color) {
        validateSku(sku);
        validatePrice(price);

        this.sku = sku;
        this.price = price;
        this.size = size;
        this.color = color;
    }

    void setProduct(Product product) {
        this.product = product;
    }

    public void changePrice(BigDecimal price) {
        validatePrice(price);

        this.price = price;
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be greater than or equal to zero");
        }
    }

    private void validateSku(String sku) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU must not be null or blank");
        }
    }
}