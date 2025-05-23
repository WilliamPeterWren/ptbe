package com.tranxuanphong.cartservice.dto.request;

import lombok.Getter;

@Getter
public class AddToCartRequest {
  String userId;
  String cartId;
  String variantId;
  Long quantity;
}
