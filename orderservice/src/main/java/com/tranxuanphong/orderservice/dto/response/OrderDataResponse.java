package com.tranxuanphong.orderservice.dto.response;

import java.util.Set;

import com.tranxuanphong.orderservice.entity.OrderProduct;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class OrderDataResponse {
  String id;

  String customerName;
  
  String sellerId;

  Set<OrderProduct> products;

  String status;
  String statusDetail;

  String shippingUnit;
  String shippingCode;
  
  Long shippingVoucher;
  String peterVoucher;
  Long sellerVoucher;

  String paymentMethod;
}
