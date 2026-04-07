package com.example.shop.service;

import com.example.shop.dto.product.ProductResponse;
import com.example.shop.entity.Product;
import com.example.shop.entity.ProductCategory;
import com.example.shop.entity.ProductImage;
import com.example.shop.entity.ProductVariant;
import com.example.shop.exception.*;
import com.example.shop.mapper.ProductMapper;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;

    public ProductResponse getByProductId(Long productId) {
        validateProductId(productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return ProductMapper.toProductResponse(product);
    }

    public List<Product> getProductsByCategory(ProductCategory category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        return productRepository.findByCategory(category);
    }

    public ProductVariant getVariantBySku(String sku) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("Sku cannot be null or blank");
        }

        return variantRepository.findBySku(sku)
                .orElseThrow(() -> new VariantNotFoundException(sku));
    }

    public List<Product> searchProductByName(String productName) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or blank");
        }

        return productRepository.findByNameContainingIgnoreCase(productName);
    }

    @Transactional
    public Product createProduct(String name, String description, ProductCategory category) {
        Product product = new Product(name, description, category);
        return productRepository.save(product);
    }

    @Transactional
    public ProductVariant addVariantToProduct(Long productId, String sku, BigDecimal price, String size, String color) {
        validateProductId(productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (variantRepository.existsBySku(sku)) {
            throw new VariantAlreadyExistsException(sku);
        }

        ProductVariant productVariant = new ProductVariant(sku, price, size, color);

        product.addVariant(productVariant);
        productRepository.save(product);
        return  productVariant;
    }

    @Transactional
    public ProductVariant removeVariantFromProduct(Long productId, Long variantId) {
        validateProductId(productId);
        validateVariantId(variantId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        if (variant.getProduct() == null || !variant.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("Variant does not belong to this product");
        }

        product.removeVariant(variant);
        productRepository.save(product);
        return variant;
    }

    @Transactional
    public Product addImageToProduct(Long productId, String imageUrl, String altText, int position) {
        validateProductId(productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        ProductImage image = new ProductImage(imageUrl, altText, position);

        product.addImage(image);
        return productRepository.save(product);
    }

    @Transactional
    public Product removeImageFromProduct(Long productId, Long imageId) {
        validateProductId(productId);
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

    @Transactional
    public ProductVariant changeVariantPrice(Long variantId, BigDecimal newPrice) {
        validateVariantId(variantId);

        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        variant.changePrice(newPrice);
        return variantRepository.save(variant);
    }

    private void validateProductId(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product id cannot be null");
        }
    }

    private void validateVariantId(Long variantId) {
        if (variantId == null) {
            throw new IllegalArgumentException("Variant id cannot be null");
        }
    }

}
