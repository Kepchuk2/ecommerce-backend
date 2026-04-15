package com.example.shop.repository;

import com.example.shop.entity.Product;
import com.example.shop.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
           select distinct p from Product p
           left join fetch p.productImages
           left join fetch p.variants
           """)
    List<Product> findAllWithDetails();

    @Query("""
           select distinct p from Product p
           left join fetch p.productImages
           left join fetch p.variants
           where p.id = :productId
           """)
    Optional<Product> findByIdWithDetails(@Param("productId") Long productId);

    @Query("""
           select distinct p from Product p
           left join fetch p.productImages
           left join fetch p.variants
           where p.category = :category
           """)
    List<Product> findByCategoryWithDetails(@Param("category") ProductCategory category);

    @Query("""
           select distinct p from Product p
           left join fetch p.productImages
           left join fetch p.variants
           where lower(p.name) like lower(concat('%', :productName, '%'))
           """)
    List<Product> searchByNameWithDetails(@Param("productName") String productName);
}

