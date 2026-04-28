package com.example.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

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
    private Set<ProductVariant> variants = new HashSet<>();

    public Product(String name, String description, ProductCategory category) {
        validateName(name);
        validateDescription(description);
        validateCategory(category);

        this.name = name;
        this.description = description;
        this.category = category;
    }

    public void addVariant(ProductVariant variant) {
        validateVariantCanBeAddedToProduct(variant);

        if (variants.add(variant)) {
            variant.setProduct(this);
        }
    }

    public void removeVariant(ProductVariant variant) {
        validateVariantBelongsToThisProduct(variant);

        ProductVariant existing = variants.stream()
                .filter(v -> Objects.equals(v.getSku(), variant.getSku()))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            variants.remove(existing);
            existing.setProduct(null);
        }
    }

    public void addImage(ProductImage image) {
        validateImageCanBeAddedToProduct(image);

        productImages.add(image);
        image.setProduct(this);
    }

    public void removeImage(ProductImage image) {
        validateImageBelongsToThisProduct(image);

        if (productImages.remove(image)) {
            image.setProduct(null);
        }
    }

    private void validateProductVariant(ProductVariant variant) {
        if (variant == null) {
            throw new IllegalArgumentException("Product variant must not be null");
        }
    }

    private void validateProductImage(ProductImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Product image must not be null");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name must not be null or blank");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Product description must not be null or blank");
        }
    }

    private void validateCategory(ProductCategory category) {
        if (category == null) {
            throw new IllegalArgumentException("Product category must not be null");
        }
    }

    private void validateVariantCanBeAddedToProduct(ProductVariant variant) {
        validateProductVariant(variant);

        if (variant.getProduct() != null && variant.getProduct() != this) {
            throw new IllegalStateException("Variant already belongs to another product");
        }
    }

    private void validateVariantBelongsToThisProduct(ProductVariant variant) {
        validateProductVariant(variant);

        if (variant.getProduct() != this) {
            throw new IllegalArgumentException("Variant does not belong to this product");
        }
    }

    private void validateImageCanBeAddedToProduct(ProductImage image) {
        validateProductImage(image);

        if (image.getProduct() != null && image.getProduct() != this) {
            throw new IllegalStateException("Image already belongs to another product");
        }
    }

    private void validateImageBelongsToThisProduct(ProductImage image) {
        validateProductImage(image);

        if (image.getProduct() != this) {
            throw new IllegalArgumentException("Image does not belong to this product");
        }
    }
}