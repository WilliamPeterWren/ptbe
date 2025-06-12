package com.tranxuanphong.orderservice.dto.response;

import java.time.Instant;
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
  String username;
  String sellerId;
  String sellerUsername;
  Set<ItemResponse> items;

  String recieveImage;
  String shipperId;

  Long shippingPrice;
  String shippingName;
  Long shippingVoucherPrice;
  String sellerVoucherId;
  Long peterVoucher;

  String addressId;
  Set<Status> orderStatus;
  PaymentType paymentType;
  Instant createdAt;
  Instant updatedAt;

}
