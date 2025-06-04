package com.tranxuanphong.orderservice.model;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PeterVoucherResponse {
  String id;
  String name;
  String slug;
  Long value;
  Instant expiredAt;
}
