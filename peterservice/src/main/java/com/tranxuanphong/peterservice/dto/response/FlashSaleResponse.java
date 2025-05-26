package com.tranxuanphong.peterservice.dto.response;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FlashSaleResponse {
  String id;
  String productId;
  Long price;
  Instant expiredAt;
}
