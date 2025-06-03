package com.tranxuanphong.cartservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartProductResponse {
  String productId;
  String productName;
  String variantId;
  String variantName;
  String slug;
  String image;
  Long price;
  Long saleSprice;
}
