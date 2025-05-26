package com.tranxuanphong.cartservice.dto.request;

import lombok.Getter;

@Getter
public class AddToCartRequest {
  String sellerId;
  String variantId;
  Long quantity;
}
