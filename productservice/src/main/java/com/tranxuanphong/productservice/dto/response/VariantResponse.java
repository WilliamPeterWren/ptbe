package com.tranxuanphong.productservice.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantResponse {
  String id;
  String productId;
  String variantName;
  Long price;
  Long stock;
}
