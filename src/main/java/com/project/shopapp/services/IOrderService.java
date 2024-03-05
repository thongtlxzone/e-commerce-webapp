package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.OrderEntity;

import java.util.List;

public interface IOrderService {
    OrderEntity createOrder(OrderDTO orderDTO) throws DataNotFoundException, Exception;
    OrderEntity getOrder(long id);
    List<OrderEntity> findByUserId(long userId);
    OrderEntity updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException;
    void deleteOrder(long id);
}
