package com.tranxuanphong.orderservice.dto.response;

import java.util.Set;

import com.tranxuanphong.orderservice.entity.OrderItem;
import com.tranxuanphong.orderservice.enums.PaymentType;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateOrderResponse {
  String id;
  String userId;
  Set<OrderItem> orderItems;
  Long shippingVoucherId;
  String addressId;
  PaymentType paymentType;
}
