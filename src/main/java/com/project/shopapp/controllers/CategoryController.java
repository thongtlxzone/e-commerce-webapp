package com.project.shopapp.controllers;

import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.CategoryEntity;
import com.project.shopapp.responses.CategoryReponse;
import com.project.shopapp.responses.UpdateCategoryResponse;
import com.project.shopapp.services.CategoryService;
import com.project.shopapp.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/categories")
//@Validated
//Dependency Injection
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    private final LocalizationUtils localizationUtils;
    @PostMapping("")
    public ResponseEntity<?> createdCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result
            ){
        if (result.hasErrors()){
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(CategoryReponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.CATEGORY_CREATE_FAILED,errorMessages))
                    .category(null)
                    .build()
            );
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(CategoryReponse.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.CATEGORY_CREATE_SUCCESSFULLY))
                .category(categoryDTO)
                .build()
        );
    }
    @GetMapping("")
    public ResponseEntity<?> getAllCategory(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        List<CategoryEntity> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable long id,
            @RequestBody @Valid CategoryDTO categoryDTO
    ){
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(UpdateCategoryResponse.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.CATEGORY_UPDATE_SUCCESSFULLY,id))
                .build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable long id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(localizationUtils.getLocalizedMessage(MessageKeys.CATEGORY_DELETE_SUCCESSFULLY), HttpStatus.OK);
    }
}
