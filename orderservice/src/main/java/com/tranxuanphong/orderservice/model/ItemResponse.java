package com.tranxuanphong.orderservice.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemResponse {
  Long salePrice;
  Long discount;
  Long price;
  String variantId;
  String variantName;
  String productId;
  String productName;
  Long quantity;
  String image;

  boolean alreadyReview;
}
