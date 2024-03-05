package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @Column(name = "fullname", length = 100)
    private String fullname;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name = "phone",nullable = false, length = 100)
    private String phoneNumber;
    @Column(name = "address", length = 100)
    private String address;
    @Column(name = "note", length = 100)
    private String note;
    @Column(name = "order_date")
    private Date orderDate;
    @Column(name = "status")
    private String status;
    @Column(name = "total_money")
    private float totalMoney;
    @Column(name = "shipping_method")
    private String shippingMethod;
    @Column(name = "shipping_address")
    private String shippingAddress;
    @Column(name = "shipping_date")
    private LocalDate shippingDate;
    @Column(name = "tracking_number")
    private String trackingNumber;
    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "active")
    private boolean active; // thuộc về admin
}
