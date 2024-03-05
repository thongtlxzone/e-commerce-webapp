package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.BaseEntity;
import com.project.shopapp.models.ProductEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {
    private String name;
    private float price;
    private String thumbnail;
    private String description;
    private long categoryId;
    public static ProductResponse fromProduct(ProductEntity productEntity){
        ProductResponse productResponse = ProductResponse.builder()
                .name(productEntity.getName())
                .price(productEntity.getPrice())
                .thumbnail(productEntity.getThumbnail())
                .description(productEntity.getDescription())
                .categoryId(productEntity.getCategory().getId())
                .build();
        productResponse.setUpdatedAt(productEntity.getUpdatedAt());
        productResponse.setCreatedAt(productEntity.getUpdatedAt());
        return productResponse;
    }
}
