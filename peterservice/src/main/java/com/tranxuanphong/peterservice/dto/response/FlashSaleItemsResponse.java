package com.tranxuanphong.peterservice.dto.response;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class FlashSaleItemsResponse {
  String id;
  Set<String> images;
  String productName;
  String slug;
  Long price;
  Long salePrice;
  Long discount;
  String username;
  Long stock;
}
