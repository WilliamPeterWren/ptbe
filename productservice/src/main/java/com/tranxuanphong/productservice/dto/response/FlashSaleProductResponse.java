package com.tranxuanphong.productservice.dto.response;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FlashSaleProductResponse {
  String id;
  Set<String> images;
  String productName;
  String slug;
  Long price;
  Long salePrice;
  String username;
  Long stock;
}
