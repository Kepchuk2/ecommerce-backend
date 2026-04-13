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
           where p.id = :productId
           """)
    Optional<Product> findByIdWithImages(Long productId);

    @Query("""
           select distinct p from Product p
           left join fetch p.productImages
           where p.category = :category
           """)
    List<Product> findByCategoryWithImages(ProductCategory category);

    @Query("""
           select distinct p from Product p
           left join fetch p.productImages
           """)
    List<Product> findAllWithImages();

    @Query("""
           select distinct p from Product p
           left join fetch p.productImages
           where lower(p.name) like lower(concat('%', :name, '%'))
           """)
    List<Product> searchByNameWithImages(@Param("name") String name);
}

