package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "product_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageEntity {
    public static final int MAXIMUM_IMAGES_PER_PRODUCT = 5;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;
    @Column(name = "image_url", length = 255)
    private String imageUrl;
}
