package com.project.shopapp.services;

import com.project.shopapp.dtos.CartItemDTO;
import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.*;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderEntity createOrder(OrderDTO orderDTO) throws Exception {
        //Tìm xem userId có tồn tại không
        UserEntity user = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: " + orderDTO.getUserId()));
        //Convert orderDTO => OrderEntity;
        //Thư viện Model Mapper
        //Tạo 1 luồng bằng ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(OrderDTO.class, OrderEntity.class)
                .addMappings(mapper -> mapper.skip(OrderEntity::setId));
        //Cập nhật các trường của Order bằng OrderDTO
        OrderEntity order = new OrderEntity();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setTotalMoney(orderDTO.getTotalMoney());
        order.setStatus(OrderStatusEntity.PENDING);
        //Kiểm tra shipping date phải >= ngày hnay
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now(): orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Date must be at least today");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
        List<OrderDetailEntity> orderDetails = new ArrayList<>();
        for(CartItemDTO cartItem : orderDTO.getCartItems()){
            OrderDetailEntity orderDetail = new OrderDetailEntity();
            orderDetail.setOrder(order);

            Long productId = cartItem.getProductId();
            int quantity = cartItem.getQuantity();

            ProductEntity product = productRepository.findById(productId)
                    .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + productId));
            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(quantity);
            orderDetail.setPrice(product.getPrice());
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
        return order;
    }

    @Override
    public OrderEntity getOrder(long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<OrderEntity> findByUserId(long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public OrderEntity updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException {
        OrderEntity order = orderRepository.findById(id).orElseThrow(() ->
                    new DataNotFoundException("Cannot found order with id: "+ id));
        UserEntity existingUser = userRepository.findById(orderDTO.getUserId()).orElseThrow(() ->
                    new DataNotFoundException("Cannot found user with id: "+orderDTO.getUserId()));
        modelMapper.typeMap(OrderDTO.class,OrderEntity.class)
                .addMappings(mapper -> mapper.skip(OrderEntity::setId));
        modelMapper.map(orderDTO,order);
        order.setUser(existingUser);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(long id) {
        OrderEntity order = orderRepository.findById(id).orElse(null);
        //Xoa cung
//        if (order.isPresent()){
//            orderRepository.delete(order.get());
//        }
        //Xoa mem
        order.setActive(false);
        orderRepository.save(order);
    }
}
