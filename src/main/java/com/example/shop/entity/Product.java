package com.example.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> variants = new ArrayList<>();

    public Product(String name, String description, ProductCategory category) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name must not be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Product description must not be null or blank");
        }
        if (category == null) {
            throw new IllegalArgumentException("Product category must not be null");
        }

        this.name = name;
        this.description = description;
        this.category = category;
    }

    public void addVariant(ProductVariant variant) {
        validationProductVariant(variant);
        if (variant.getProduct() != null && variant.getProduct() != this) {
            throw new IllegalStateException("Variant already belongs to another product");
        }
        if (variants.contains(variant)) {
            return;
        }

        variants.add(variant);
        variant.setProduct(this);
    }

    public void removeVariant(ProductVariant variant) {
        validationProductVariant(variant);

        if (variants.remove(variant)) {
            variant.setProduct(null);
        }
    }

    public void addImage(ProductImage image) {
        validationProductImage(image);

        if (image.getProduct() != null && image.getProduct() != this) {
            throw new IllegalStateException("Image already belongs to another product");
        }
        if (productImages.contains(image)) {
            return;
        }

        productImages.add(image);
        image.setProduct(this);
    }

    public void removeImage(ProductImage image) {
        validationProductImage(image);

        if (productImages.remove(image)) {
            image.setProduct(null);
        }
    }

    private void validationProductVariant(ProductVariant variant) {
        if (variant == null) {
            throw new IllegalArgumentException("Product variant must not be null");
        }
    }

    private void validationProductImage(ProductImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Product image must not be null");
        }
    }
}