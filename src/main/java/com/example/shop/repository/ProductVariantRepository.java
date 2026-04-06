package com.example.shop.repository;

import com.example.shop.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    //Получить все варианты конкретного продукта
    List<ProductVariant> findByProductId(Long productId);

    //Поиск по sku
    Optional<ProductVariant> findBySku(String sku);

    //Фильтр по размеру и цвету и продукту
    Optional<ProductVariant> findByProductIdAndColorAndSize(Long productId, String color, String size);

    boolean existsBySku(String sku);
}
