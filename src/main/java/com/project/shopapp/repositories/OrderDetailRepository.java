package com.project.shopapp.repositories;

import com.project.shopapp.models.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity,Long> {
    List<OrderDetailEntity> findByOrderId(Long orderId);
}
