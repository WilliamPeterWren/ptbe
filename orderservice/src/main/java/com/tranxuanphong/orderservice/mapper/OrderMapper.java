package com.tranxuanphong.orderservice.mapper;


import java.util.List;

import org.mapstruct.Mapper;
// import org.mapstruct.MappingTarget;

import com.tranxuanphong.orderservice.dto.request.OrderCreateRequest;
// import com.tranxuanphong.orderservice.dto.request.OrderUpdateRequest;
import com.tranxuanphong.orderservice.dto.response.OrderResponse;
import com.tranxuanphong.orderservice.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
  Order toOrder(OrderCreateRequest request);
  // void updateOrder(@MappingTarget Order user, OrderUpdateRequest request);
  OrderResponse toOrderResponse(Order user);
  List<OrderResponse> toListOrderResponse(List<Order> listOrder);
}
