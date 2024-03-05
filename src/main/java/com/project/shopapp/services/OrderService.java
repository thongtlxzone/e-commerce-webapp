package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.OrderEntity;
import com.project.shopapp.models.OrderStatusEntity;
import com.project.shopapp.models.UserEntity;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
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
        order.setStatus(OrderStatusEntity.PENDING);
        //Kiểm tra shipping date phải >= ngày hnay
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now(): orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Date must be at least today");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
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
