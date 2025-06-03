package com.tranxuanphong.peterservice.dto.response;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
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
