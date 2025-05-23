package com.tranxuanphong.orderservice.dto.request;

import lombok.Getter;

@Getter
public class UpdateOrderRequest {
  String userId;
  String cartId;
  String cartItemId;
  Long quantity;
}
