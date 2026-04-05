package com.example.shop.service;

import com.example.shop.entity.Product;
import com.example.shop.entity.ProductCategory;
import com.example.shop.exception.ProductNotFoundException;
import com.example.shop.repository.ProductImageRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
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

    public Optional<Product> getVariantBySku(String sku) {

    }

    public List<Product> searchProductByName(String productName) {

    }
}
