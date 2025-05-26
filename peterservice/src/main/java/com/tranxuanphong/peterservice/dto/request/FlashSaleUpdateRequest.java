package com.tranxuanphong.peterservice.dto.request;

import java.time.Instant;

import lombok.Getter;

@Getter
public class FlashSaleUpdateRequest {
  String productId;
  Long price;
  Instant expiredAt;
}
