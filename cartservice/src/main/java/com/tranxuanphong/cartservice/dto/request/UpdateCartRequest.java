package com.tranxuanphong.cartservice.dto.request;

import lombok.Getter;

@Getter
public class UpdateCartRequest {
  String userId;
  String cartId;
  String cartItemId;
  Long quantity;
}
