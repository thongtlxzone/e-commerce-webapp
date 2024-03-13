package com.project.shopapp.services;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.CategoryEntity;
import com.project.shopapp.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
//    @Autowired
//    CategoryRepository categoryRepository;
    private final CategoryRepository categoryRepository;
//    @RequiredArgsContructor
//    public CategoryService(CategoryRepository categoryRepository){
//        this.categoryRepository = categoryRepository;
//    }
    @Override
    @Transactional
    public CategoryEntity createCategory(CategoryDTO categoryDTO) {
        CategoryEntity newCategory = CategoryEntity
                .builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(newCategory);
    }

    @Override
    public CategoryEntity getCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public CategoryEntity updateCategory(long categoryId, CategoryDTO categoryDTO) {
        CategoryEntity existingCategory = getCategoryById(categoryId);
        existingCategory.setName(categoryDTO.getName());
        categoryRepository.save(existingCategory);
        return existingCategory;
    }

    @Override
    @Transactional
    public void deleteCategory(long id) {
        //Xóa cứng -> xóa trên database
        categoryRepository.deleteById(id);

    }
}
