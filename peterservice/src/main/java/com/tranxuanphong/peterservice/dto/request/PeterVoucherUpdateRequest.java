package com.tranxuanphong.peterservice.dto.request;

import java.time.Instant;

import lombok.Getter;

@Getter
public class PeterVoucherUpdateRequest {
  String name;
  Long value;
  Instant expiredAt;
}
