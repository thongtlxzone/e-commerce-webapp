package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.OrderDetailEntity;
import com.project.shopapp.models.OrderEntity;
import com.project.shopapp.models.ProductEntity;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    @Override
    @Transactional
    public OrderDetailEntity createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException, Exception {
        OrderEntity order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot found order with id: " + orderDetailDTO.getOrderId()));
        ProductEntity product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot found order with id: " + orderDetailDTO.getProductId()));
        OrderDetailEntity orderDetail = OrderDetailEntity.builder()
                .order(order)
                .product(product)
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .price(orderDetailDTO.getPrice())
                .totalMoney(orderDetailDTO.getTotalOfMoney())
                .color(orderDetailDTO.getColor())
                .build();
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetailEntity getOrderDetail(long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot found order detail with id "+ id));
    }

    @Override
    public List<OrderDetailEntity> findByOrderId(long userId) {
        return orderDetailRepository.findByOrderId(userId);
    }

    @Override
    @Transactional
    public OrderDetailEntity updateOrderDetail(long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        OrderDetailEntity existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order detail with id " + id));
        OrderEntity existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id " + id));
        ProductEntity existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id " + id));
        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalOfMoney());
        existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);
        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    @Transactional
    public void deleteOrderDetail(long id) {
        orderDetailRepository.deleteById(id);
    }
}
