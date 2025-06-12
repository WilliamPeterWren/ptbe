package com.tranxuanphong.cartservice.dto.model;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ItemResponse {
  String productId;
  String productName;
  String variantId;
  String variantName;
  String slug;

  @Builder.Default
  Long quantity = 0L;
  String image;
  Long price;
  Long salePrice;
  Long discount;

  
  @Builder.Default
  Instant updatedAt = Instant.now();
}
