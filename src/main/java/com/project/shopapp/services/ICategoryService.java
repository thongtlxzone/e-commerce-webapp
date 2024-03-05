package com.project.shopapp.services;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.CategoryEntity;

import java.util.List;

public interface ICategoryService {
    CategoryEntity createCategory(CategoryDTO category);
    CategoryEntity getCategoryById(long id);
    List<CategoryEntity> getAllCategories();
    CategoryEntity updateCategory(long categoryId, CategoryDTO categoryDTO);
    void deleteCategory(long id);
}
