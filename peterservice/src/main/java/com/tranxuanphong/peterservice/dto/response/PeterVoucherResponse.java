package com.tranxuanphong.peterservice.dto.response;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PeterVoucherResponse {
  String name;
  String slug;
  Long value;
  Instant expiredAt;
}
