package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.OrderDetailEntity;
import com.project.shopapp.models.OrderEntity;
import com.project.shopapp.models.ProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {
    private Long id;
    @JsonProperty("order_id")
    private long orderId;
    @JsonProperty("product_id")
    private long productId;
    @JsonProperty("price")
    private float price;
    @JsonProperty("number_of_products")
    private int numberOfProducts;
    @JsonProperty("total_money")
    private float totalMoney;
    @JsonProperty("color")
    private String color;
    public static OrderDetailResponse fromOrderDetail(OrderDetailEntity orderDetail){
        return OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .productId(orderDetail.getProduct().getId())
                .price(orderDetail.getPrice())
                .numberOfProducts(orderDetail.getNumberOfProducts())
                .totalMoney(orderDetail.getTotalMoney())
                .color(orderDetail.getColor())
                .build();
    }
}
