package com.tranxuanphong.cartservice.dto.request;

import lombok.Getter;

@Getter
public class CartUpdateRequest {
  String variantId;
  Long quantity;
}
