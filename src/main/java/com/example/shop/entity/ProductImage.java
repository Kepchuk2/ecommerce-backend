package com.example.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_images",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"product_id", "position"})
        })
@Getter
@NoArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String imageUrl;

    private String altText;

    @Column(nullable = false)
    private int position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public ProductImage(String imageUrl, String altText, int position) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("Image URL must not be blank");
        }
        if (position < 0) {
            throw new IllegalArgumentException("Image position must be greater than or equal to zero");
        }

        this.imageUrl = imageUrl;
        this.altText = (altText != null && altText.isBlank()) ? null : altText;
        this.position = position;
    }

    void setProduct(Product product) {
        this.product = product;
    }
}