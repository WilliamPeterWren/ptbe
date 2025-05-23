package com.tranxuanphong.orderservice.service;
import org.springframework.stereotype.Service;

import com.tranxuanphong.orderservice.dto.request.CreateOrderRequest;
import com.tranxuanphong.orderservice.dto.request.UpdateOrderRequest;
import com.tranxuanphong.orderservice.dto.response.OrderResponse;
import com.tranxuanphong.orderservice.entity.Order;
import com.tranxuanphong.orderservice.entity.Status;
import com.tranxuanphong.orderservice.enums.OrderStatus;
import com.tranxuanphong.orderservice.exception.AppException;
import com.tranxuanphong.orderservice.exception.ErrorCode;
import com.tranxuanphong.orderservice.mapper.OrderMapper;
import com.tranxuanphong.orderservice.repository.OrderRepository;
import com.tranxuanphong.orderservice.repository.httpclient.UserClient;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class OrderService {
  OrderRepository orderRepository;
  UserClient userClient;
  OrderMapper orderMapper;

  public OrderResponse create(CreateOrderRequest request){

    boolean response = userClient.existsId(request.getUserId());

    if(!response){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }


    Set<Status> setStatus = new HashSet<>();

    Status status = Status.builder()
    .status(OrderStatus.PENDING)
    .build();

    setStatus.add(status);

    Order order = orderMapper.toOrder(request);

    order.setOrderStatus(setStatus);

    return orderMapper.toOrderResponse(orderRepository.save(order));
  }

  // public List<OrderResponse> getAll(){
  //   return orderMapper.toListOrderResponse(orderRepository.findAll());
  // }

 

  // public OrderResponse getOne(String slug){
  //   Order order = orderRepository.findBySlug(slug).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTS));
  //   return orderMapper.toOrderResponse(order);
  // }

  // public OrderResponse update(String slug, UpdateOrderRequest request){
  //   Order order = orderRepository.findBySlug(slug).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTS));
    
  //   if(orderRepository.existsByOrderName(request.getOrderName())){
  //     throw new AppException(ErrorCode.CATEGORY_EXISTS);
  //   }

  //   orderMapper.updateOrder(order, request);
    
  //   return orderMapper.toOrderResponse(order);
  // }

}
