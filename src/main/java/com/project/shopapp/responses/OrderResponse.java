package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.OrderDetailEntity;
import com.project.shopapp.models.OrderEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("fullname")
    private String fullname;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("email")
    private String email;
    @JsonProperty("address")
    private String address;
    @JsonProperty("note")
    private String note;
    @JsonProperty("order_date")
    private LocalDate orderDate;
    @JsonProperty("status")
    private String status;
    @JsonProperty("total_money")
    private double totalMoney;
    @JsonProperty("shipping_method")
    private String shippingMethod;
    @JsonProperty("shipping_address")
    private String shippingAddress;
    @JsonProperty("shipping_date")
    private LocalDate shippingDate;
    @JsonProperty("shipping_method")
    private String paymentMethod;
    @JsonProperty("order_details")
    private List<OrderDetailEntity> orderDetails;
    public static OrderResponse fromOrder(OrderEntity order){
        return OrderResponse
                .builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .orderDate(order.getOrderDate())
                .orderDetails(order.getOrderDetails())
                .address(order.getAddress())
                .email(order.getEmail())
                .fullname(order.getFullname())
                .note(order.getNote())
                .paymentMethod(order.getPaymentMethod())
                .phoneNumber(order.getPhoneNumber())
                .shippingAddress(order.getShippingAddress())
                .shippingMethod(order.getShippingMethod())
                .status(order.getStatus())
                .shippingDate(order.getShippingDate())
                .totalMoney(order.getTotalMoney())
                .build();
    }
}
