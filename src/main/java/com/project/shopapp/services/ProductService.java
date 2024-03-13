package com.project.shopapp.services;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamExeption;
import com.project.shopapp.models.CategoryEntity;
import com.project.shopapp.models.ProductEntity;
import com.project.shopapp.models.ProductImageEntity;
import com.project.shopapp.repositories.CategoryRepository;
import com.project.shopapp.repositories.ProductImageRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.responses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    @Override
    @Transactional
    public ProductEntity createProduct(ProductDTO productDTO) throws DataNotFoundException {
        CategoryEntity existingCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(() ->
                        new DataNotFoundException("Cannot find category with id "+ productDTO.getCategoryId()));
        ProductEntity newProduct = ProductEntity.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .category(existingCategory)
                .description(productDTO.getDescription())
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public ProductEntity getProductById(long productId) throws Exception {
        return productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find product with id "+productId));
    }

    @Override
    public List<ProductEntity> findProductsByIds(List<Long> productIds) {
        return productRepository.findProductsByIds(productIds);
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest) {
        //Lấy danh sách sản phẩm theo trang(page) và giới hạn(limit)
        Page<ProductEntity> productsPage;
        productsPage = productRepository.searchProducts(categoryId,keyword,pageRequest);
        return productsPage.map(ProductResponse::fromProduct);
    }

    @Override
    @Transactional
    public ProductEntity updateProduct(
            long id,
            ProductDTO productDTO
    )
            throws Exception {
        ProductEntity existingProduct = getProductById(id);
        if (existingProduct!= null){
            //copy các thuộc tính(attributes) từ DTO -> Product
            //Có thể sử dụng ModelMapper
            CategoryEntity existingCategory = categoryRepository
                    .findById(id)
                    .orElseThrow(() ->
                            new DataNotFoundException("Cannot find category with id "+ productDTO.getCategoryId()));
            existingProduct.setName(productDTO.getName());
            existingProduct.setCategory(existingCategory);
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteProduct(long id) {
        Optional<ProductEntity> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }
    //ProductImageService
    @Override
    @Transactional
    public ProductImageEntity createProductImage(long productId, ProductImageDTO productImageDTO) throws Exception {
        ProductEntity existingProduct = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new DataNotFoundException("Cannot find product with id "+productImageDTO.getProductId()));
        ProductImageEntity newProductImage = ProductImageEntity.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImgUrl())
                .build();
        //Không cho insert quá 5 ảnh trong 1 sản phẩm
        int size = productImageRepository.findByProductId(productId).size();
        if (size>=ProductImageEntity.MAXIMUM_IMAGES_PER_PRODUCT){
            throw new InvalidParamExeption(
                    "Number of images must be < " + ProductImageEntity.MAXIMUM_IMAGES_PER_PRODUCT);
        }
        return productImageRepository.save(newProductImage);
    }
}
