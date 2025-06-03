package com.tranxuanphong.orderservice.dto.request;


import java.util.Set;

import com.tranxuanphong.orderservice.entity.OrderItem;
import com.tranxuanphong.orderservice.enums.PaymentType;

import lombok.Getter;

@Getter
public class OrderCreateRequest {
  
  String sellerId;
  
  Set<OrderItem> orderItems;
  
  String shippingId;
  
  String shippingVoucherId;

  String sellerVoucherId;

  String addressId;

  PaymentType paymentType;
}
