package com.project.shopapp.repositories;

import com.project.shopapp.models.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

}
