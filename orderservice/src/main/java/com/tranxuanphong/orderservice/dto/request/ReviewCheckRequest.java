package com.tranxuanphong.orderservice.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewCheckRequest {
  String userId;
  String productId;
  String variantId;
  String orderId;
}
