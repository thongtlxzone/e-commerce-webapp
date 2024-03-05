package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    @Column(name = "price")
    private float price;
    @Column(name = "thumbnail", nullable = false, length = 255)
    private String thumbnail;
    @Column(name = "description")
    private String description;
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    @PrePersist
//    protected void onCreated(){
//        createdAt = LocalDateTime.now();
//        updatedAt = LocalDateTime.now();
//    }
//    @PreUpdate
//    protected void onUpdated(){
//        updatedAt = LocalDateTime.now();
//    }
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
}
