package com.example.shop.service;

import com.example.shop.entity.Product;
import com.example.shop.entity.ProductCategory;
import com.example.shop.entity.ProductImage;
import com.example.shop.entity.ProductVariant;
import com.example.shop.exception.ImageNotFoundException;
import com.example.shop.exception.ProductNotFoundException;
import com.example.shop.exception.VariantAlreadyExistsException;
import com.example.shop.exception.VariantNotFoundException;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;

    public Product getByProductId(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product id cannot be null");
        }

        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public List<Product> getProductsByCategory(ProductCategory category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        return productRepository.findByCategory(category);
    }

    public Optional<ProductVariant> getVariantBySku(String sku) {
        if (sku == null) {
            throw new IllegalArgumentException("Sku cannot be null");
        }

        return variantRepository.findBySku(sku);
    }

    public List<Product> searchProductByName(String productName) {
        if (productName == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        }

        return productRepository.findByNameContainingIgnoreCase(productName);
    }

    public Product createProduct(String name, String description, ProductCategory category) {
        Product product = new Product(name, description, category);
        return productRepository.save(product);
    }

    public Product addVariantToProduct(Long productId, String sku, BigDecimal price, String size, String color) {
        if (productId == null) {
            throw new IllegalArgumentException("Product id cannot be null");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (variantRepository.existsBySku(sku)) {
            throw new VariantAlreadyExistsException(sku);
        }

        ProductVariant productVariant = new ProductVariant(sku, price, size, color);

        product.addVariant(productVariant);
        return productRepository.save(product);
    }

    public Product removeVariantFromProduct(Long productId, Long variantId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product id cannot be null");
        }
        if (variantId == null) {
            throw new IllegalArgumentException("Variant id cannot be null");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        if (variant.getProduct() == null || !variant.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("Variant does not belong to this product");
        }

        product.removeVariant(variant);
        return productRepository.save(product);
    }

    public Product addImageToProduct(Long productId, String imageUrl, String altText, int position) {
        if (productId == null) {
            throw new IllegalArgumentException("Product id cannot be null");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        ProductImage image = new ProductImage(imageUrl, altText, position);

        product.addImage(image);
        return productRepository.save(product);
    }

    public Product removeImageFromProduct(Long productId, Long imageId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product id cannot be null");
        }
        if (imageId == null) {
            throw new IllegalArgumentException("Image id cannot be null");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        ProductImage image = product.getProductImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new ImageNotFoundException(imageId));

        product.removeImage(image);

        return  productRepository.save(product);
    }

    public ProductVariant changeVariantPrice(Long variantId, BigDecimal newPrice) {
        if (variantId == null) {
            throw new IllegalArgumentException("Variant id cannot be null");
        }

        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        variant.changePrice(newPrice);
        return variantRepository.save(variant);
    }

}
