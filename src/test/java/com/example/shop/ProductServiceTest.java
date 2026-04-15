package com.example.shop;

import com.example.shop.entity.Product;
import com.example.shop.entity.ProductCategory;
import com.example.shop.entity.ProductVariant;
import com.example.shop.exception.ProductNotFoundException;
import com.example.shop.exception.VariantAlreadyExistsException;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.ProductVariantRepository;
import com.example.shop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductVariantRepository variantRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getByProductId_shouldReturnProduct_whenExists() {
        Long productId = 1L;
        Product product = createProduct(productId, "Hoodie", "Warm hoodie", ProductCategory.CLOTHING);

        when(productRepository.findByIdWithImages(productId)).thenReturn(Optional.of(product));

        try (MockedStatic<Hibernate> mockedHibernate = mockStatic(Hibernate.class)) {
            Product result = productService.getByProductId(productId);

            assertNotNull(result);
            assertEquals(productId, result.getId());
            assertEquals("Hoodie", result.getName());

            verify(productRepository).findByIdWithImages(productId);
        }
    }

    @Test
    void getByProductId_shouldThrow_whenNotFound() {
        Long productId = 1L;

        when(productRepository.findByIdWithImages(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> productService.getByProductId(productId));

        verify(productRepository).findByIdWithImages(productId);
    }

    @Test
    void createProduct_shouldSaveProduct() {
        Product savedProduct = createProduct(1L, "Hoodie", "Warm hoodie", ProductCategory.CLOTHING);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(productRepository.findByIdWithImages(1L)).thenReturn(Optional.of(savedProduct));

        try (MockedStatic<Hibernate> mockedHibernate = mockStatic(Hibernate.class)) {
            Product result = productService.createProduct("Hoodie", "Warm hoodie", ProductCategory.CLOTHING);

            assertNotNull(result);
            assertEquals("Hoodie", result.getName());
            assertEquals(ProductCategory.CLOTHING, result.getCategory());

            verify(productRepository).save(any(Product.class));
            verify(productRepository).findByIdWithImages(1L);
        }
    }

    @Test
    void addVariantToProduct_shouldAddVariant_whenSkuUnique() {
        Long productId = 1L;
        Product product = createProduct(productId, "Hoodie", "Warm hoodie", ProductCategory.CLOTHING);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(variantRepository.existsBySku("SKU-001")).thenReturn(false);
        when(productRepository.save(product)).thenReturn(product);

        ProductVariant result = productService.addVariantToProduct(
                productId,
                "SKU-001",
                new BigDecimal("99.99"),
                "M",
                "Black"
        );

        assertNotNull(result);
        assertEquals("SKU-001", result.getSku());
        assertEquals(1, product.getVariants().size());

        verify(productRepository).findById(productId);
        verify(variantRepository).existsBySku("SKU-001");
        verify(productRepository).save(product);
    }

    @Test
    void addVariantToProduct_shouldThrow_whenSkuAlreadyExists() {
        Long productId = 1L;
        Product product = createProduct(productId, "Hoodie", "Warm hoodie", ProductCategory.CLOTHING);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(variantRepository.existsBySku("SKU-001")).thenReturn(true);

        assertThrows(VariantAlreadyExistsException.class,
                () -> productService.addVariantToProduct(
                        productId,
                        "SKU-001",
                        new BigDecimal("99.99"),
                        "M",
                        "Black"
                ));

        verify(productRepository).findById(productId);
        verify(variantRepository).existsBySku("SKU-001");
        verify(productRepository, never()).save(any());
    }

    @Test
    void changeVariantPrice_shouldUpdatePrice() {
        Long variantId = 10L;
        ProductVariant variant = createVariant(variantId, "SKU-001", new BigDecimal("99.99"));

        when(variantRepository.findById(variantId)).thenReturn(Optional.of(variant));
        when(variantRepository.save(variant)).thenReturn(variant);

        ProductVariant result = productService.changeVariantPrice(variantId, new BigDecimal("149.99"));

        assertNotNull(result);
        assertEquals(new BigDecimal("149.99"), result.getPrice());

        verify(variantRepository).findById(variantId);
        verify(variantRepository).save(variant);
    }

    @Test
    void removeVariantFromProduct_shouldRemoveVariant() {
        Long productId = 1L;
        Long variantId = 10L;

        Product product = createProduct(productId, "Hoodie", "Warm hoodie", ProductCategory.CLOTHING);
        ProductVariant variant = createVariant(variantId, "SKU-001", new BigDecimal("99.99"));
        product.addVariant(variant);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(variantRepository.findById(variantId)).thenReturn(Optional.of(variant));
        when(productRepository.save(product)).thenReturn(product);

        ProductVariant result = productService.removeVariantFromProduct(productId, variantId);

        assertNotNull(result);
        assertEquals(variantId, result.getId());
        assertTrue(product.getVariants().isEmpty());

        verify(productRepository).findById(productId);
        verify(variantRepository).findById(variantId);
        verify(productRepository).save(product);
    }

    private Product createProduct(Long id, String name, String description, ProductCategory category) {
        Product product = new Product(name, description, category);
        ReflectionTestUtils.setField(product, "id", id);
        return product;
    }

    private ProductVariant createVariant(Long id, String sku, BigDecimal price) {
        ProductVariant variant = new ProductVariant(sku, price, "M", "Black");
        ReflectionTestUtils.setField(variant, "id", id);
        return variant;
    }
}
