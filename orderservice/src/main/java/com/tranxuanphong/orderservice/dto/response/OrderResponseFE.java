package com.tranxuanphong.orderservice.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

import com.tranxuanphong.orderservice.entity.Status;
import com.tranxuanphong.orderservice.enums.PaymentType;
import com.tranxuanphong.orderservice.model.ItemResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class OrderResponseFE {
  String id;
  String userId;
  String sellerId;
  String sellerUsername;
  Set<ItemResponse> items;

  Long shippingPrice;
  Long shippingVoucherPrice;
  String sellerVoucherId;
  Long peterVoucher;

  String addressId;
  Set<Status> orderStatus;
  PaymentType paymentType;
  Instant createdAt;
  Instant updatedAt;
}
