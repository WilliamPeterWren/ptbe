package com.tranxuanphong.orderservice.dto.request;


import java.util.Set;

import com.tranxuanphong.orderservice.entity.OrderItem;
import com.tranxuanphong.orderservice.enums.PaymentType;

import lombok.Getter;

@Getter
public class CreateOrderRequest {
  
  String userId;
  
  Set<OrderItem> orderItems;
  
  Long shippingVoucherId;

  String addressId;

  PaymentType paymentType;
}
