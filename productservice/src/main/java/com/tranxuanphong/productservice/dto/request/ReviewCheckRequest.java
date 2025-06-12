package com.tranxuanphong.productservice.dto.request;

import lombok.Getter;

@Getter
public class ReviewCheckRequest {
  String userId;
  String productId;
  String variantId;
  String orderId;
}
