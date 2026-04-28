package com.example.shop.repository;

import com.example.shop.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    //Поиск по sku
    Optional<ProductVariant> findBySku(String sku);

    boolean existsBySku(String sku);
}
