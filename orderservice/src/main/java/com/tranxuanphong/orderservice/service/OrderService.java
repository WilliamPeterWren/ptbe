package com.tranxuanphong.orderservice.service;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tranxuanphong.orderservice.dto.request.OrderCreateRequest;
import com.tranxuanphong.orderservice.dto.request.OrderUpdateRequest;
import com.tranxuanphong.orderservice.dto.response.OrderResponse;
import com.tranxuanphong.orderservice.entity.Order;
import com.tranxuanphong.orderservice.entity.OrderItem;
import com.tranxuanphong.orderservice.entity.Status;
import com.tranxuanphong.orderservice.enums.OrderStatus;
import com.tranxuanphong.orderservice.exception.AppException;
import com.tranxuanphong.orderservice.exception.ErrorCode;
import com.tranxuanphong.orderservice.mapper.OrderMapper;
import com.tranxuanphong.orderservice.repository.httpclient.UserClient;
import com.tranxuanphong.orderservice.repository.mongo.OrderRepository;
import com.tranxuanphong.orderservice.repository.httpclient.ProductClient;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class OrderService {
  OrderRepository orderRepository;

  UserClient userClient;
  ProductClient productClient;

  OrderMapper orderMapper;

  @PreAuthorize("hasRole('ROLE_USER')")
  public OrderResponse create(OrderCreateRequest request){

    String customerEmail = SecurityContextHolder.getContext().getAuthentication().getName(); 
    String customerId = userClient.userId(customerEmail);

    if(!userClient.existsId(request.getSellerId())){
      throw new AppException(ErrorCode.SELLERID_INVALID);
    }

    if(customerId.equals(request.getSellerId())){
      throw new AppException(ErrorCode.CUSTOMER_SELLER_CONFLIC);
    }

    Set<OrderItem> orderItems = request.getOrderItems();
    for(OrderItem orderItem : orderItems){
      if(!productClient.doesVariantExistBySellerId(orderItem.getVariantId(), request.getSellerId())){
        throw new AppException(ErrorCode.VARIANT_NOT_EXISTS);
      }
    }

    Set<Status> setStatus = new HashSet<>();

    Status status = Status.builder()
    .status(OrderStatus.PENDING)
    .build();

    setStatus.add(status);

    Order order = orderMapper.toOrder(request);

    order.setOrderStatus(setStatus);
    order.setUserId(customerId);

    return orderMapper.toOrderResponse(orderRepository.save(order));
  }

  public boolean doesAddressExist(String addressId){
    return orderRepository.existsByAddressId(addressId);
  }

  @PreAuthorize("hasRole('ROLE_SHIPPER')")
  public OrderResponse update(String orderId, OrderUpdateRequest request){
    String customerEmail = SecurityContextHolder.getContext().getAuthentication().getName(); 
    String customerId = userClient.userId(customerEmail);

    Order order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

    if(!order.getUserId().equals(customerId)){
      throw new AppException(ErrorCode.ORDER_USER_NOT_FOUND);
    }

    Set<Status> status = order.getOrderStatus();
    status.add(request.getOrderStatus());

    order.setOrderStatus(status);

    return orderMapper.toOrderResponse(orderRepository.save(order));
  }


  

}
