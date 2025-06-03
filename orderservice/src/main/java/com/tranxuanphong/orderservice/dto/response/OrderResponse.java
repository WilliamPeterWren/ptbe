package com.tranxuanphong.orderservice.dto.response;

import java.time.Instant;
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
  String shippingId;
  String shippingVoucherId;
  String sellerVoucherId;
  String addressId;
  Set<Status> orderStatus;
  PaymentType paymentType;
  Instant createdAt;
  Instant updatedAt;
}
