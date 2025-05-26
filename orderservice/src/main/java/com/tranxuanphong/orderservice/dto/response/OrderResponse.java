package com.tranxuanphong.orderservice.dto.response;

import java.time.LocalDate;
import java.util.Set;

import com.tranxuanphong.orderservice.entity.OrderItem;
import com.tranxuanphong.orderservice.entity.Status;
import com.tranxuanphong.orderservice.enums.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class OrderResponse {
  String id;
  String userId;
  String sellerId;
  Set<OrderItem> orderItems;
  Long shippingVoucherId;
  String addressId;
  Set<Status> orderStatus;
  PaymentType paymentType;
  LocalDate createdAt;
  LocalDate updatedAt;
}
