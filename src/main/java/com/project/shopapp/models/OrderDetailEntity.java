package com.project.shopapp.models;

import com.project.shopapp.responses.OrderDetailResponse;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "order_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;
    @Column(name = "price", nullable = false)
    private float price;
    @Column(name = "number_of_products", nullable = false)
    private int numberOfProducts;
    @Column(name = "total_money",nullable = false)
    private float totalMoney;
    @Column(name = "color")
    private String color;
}
