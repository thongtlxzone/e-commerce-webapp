package com.project.shopapp.repositories;

import com.project.shopapp.models.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    boolean existsByName(String title);
    Page<ProductEntity> findAll(Pageable pageable);//Phân trang các sản phẩm
    @Query(value = "SELECT p FROM products p WHERE " +
            "(:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId) " +
            "AND (:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword% OR p.description LIKE %:keyword%)")
    Page<ProductEntity> searchProducts(@Param("categoryId") Long categoryId, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM products p Where p.id IN :productIds")
    List<ProductEntity> findProductsByIds(@Param("productIds") List<Long> productIds);
}
