package com.project.shopapp.repositories;

import com.project.shopapp.models.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;
import java.awt.print.Pageable;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    boolean existsByName(String title);
//    Page<ProductEntity> findAll(Pageable pageable);//Phân trang các sản phẩm
}
