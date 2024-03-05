package com.project.shopapp.services;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.ProductEntity;
import com.project.shopapp.models.ProductImageEntity;
import com.project.shopapp.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    ProductEntity createProduct(ProductDTO productDTO) throws Exception;
    ProductEntity getProductById(long productId) throws Exception;
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    ProductEntity updateProduct(long id, ProductDTO productDTO) throws Exception;
    void deleteProduct(long id);
    boolean existsByName(String name);

    //ProductImageService
    ProductImageEntity createProductImage(long productId, ProductImageDTO productImageDTO) throws Exception;
}
