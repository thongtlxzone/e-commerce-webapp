package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.BaseEntity;
import com.project.shopapp.models.ProductEntity;
import com.project.shopapp.models.ProductImageEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {
    private Long id;
    private String name;
    private float price;
    private String thumbnail;
    private String description;
    @JsonProperty("category_id")
    private long categoryId;
    @JsonProperty("product_images")
    private List<ProductImageEntity> productImages = new ArrayList<>();
    public static ProductResponse fromProduct(ProductEntity productEntity){
        ProductResponse productResponse = ProductResponse.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .price(productEntity.getPrice())
                .thumbnail(productEntity.getThumbnail())
                .description(productEntity.getDescription())
                .categoryId(productEntity.getCategory().getId())
                .productImages(productEntity.getProductImages())
                .build();
        productResponse.setUpdatedAt(productEntity.getUpdatedAt());
        productResponse.setCreatedAt(productEntity.getUpdatedAt());
        return productResponse;
    }
}
