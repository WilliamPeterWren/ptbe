package com.tranxuanphong.peterservice.dto.request;

import java.time.Instant;

import lombok.Getter;

@Getter
public class FlashSaleCreateRequest {
  String name;
  Instant startedAt;
  Instant expiredAt;
}
