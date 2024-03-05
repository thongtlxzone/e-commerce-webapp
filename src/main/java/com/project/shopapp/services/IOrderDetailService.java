package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.OrderDetailEntity;

import java.util.List;

public interface IOrderDetailService {
    OrderDetailEntity createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException, Exception;
    OrderDetailEntity getOrderDetail(long id) throws DataNotFoundException;
    List<OrderDetailEntity> findByOrderId(long userId);
    OrderDetailEntity updateOrderDetail(long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
    void deleteOrderDetail(long id);
}
